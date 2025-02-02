package student.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
    private Databases() {
    }

    private static final Map<String, DataSource> DATASOURCE = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String, Connection>> THREAD_CONNECTION = new ConcurrentHashMap<>();

    private record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {
    }

    private record XaConsumer<T>(Consumer<Connection> consumer, String jdbcUrl) {
    }

    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl, Integer isolationLevel) throws SQLException {
        Connection connection = null;
        int level = (isolationLevel != null) ? isolationLevel : Connection.TRANSACTION_SERIALIZABLE;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(level);
            T result;
            result = function.apply(connection);
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(e);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static <T> T xaTransaction(Integer isolationLevel, XaFunction<T>... actions) throws SQLException {
        UserTransaction userTransaction = new UserTransactionImp();
        int level = (isolationLevel != null) ? isolationLevel : Connection.TRANSACTION_SERIALIZABLE;
        try {
            userTransaction.begin();
            T result = null;
            for (XaFunction<T> action : actions) {
                Connection connection = connection(action.jdbcUrl);
                connection.setTransactionIsolation(level);
                result = action.function.apply(connection(action.jdbcUrl));
            }
            userTransaction.commit();
            return result;
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static void transaction(Consumer<Connection> consumer, String jdbcUrl, Integer isolationLevel) {
        Connection connection = null;
        int level = (isolationLevel != null) ? isolationLevel : Connection.TRANSACTION_SERIALIZABLE;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(level);
            consumer.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(e);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static void xaTransaction(Integer isolationLevel, XaConsumer... actions) {
        UserTransaction userTransaction = new UserTransactionImp();
        int level = (isolationLevel != null) ? isolationLevel : Connection.TRANSACTION_SERIALIZABLE;
        try {
            userTransaction.begin();
            for (XaConsumer action : actions) {
                Connection connection = connection(action.jdbcUrl);
                connection.setTransactionIsolation(level);
                action.consumer.accept(connection(action.jdbcUrl));
            }
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private static DataSource dataSource(String jdbcUrl) {
        return DATASOURCE.computeIfAbsent(
                jdbcUrl,
                key -> {
                    AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
                    final String uniqId = StringUtils.substringAfter(jdbcUrl, "5432/");
                    dataSourceBean.setUniqueResourceName(uniqId);
                    dataSourceBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
                    Properties properties = new Properties();
                    properties.put("URL", jdbcUrl);
                    properties.put("user", "postgres");
                    properties.put("password", "secret");
                    dataSourceBean.setXaProperties(properties);
                    dataSourceBean.setPoolSize(10);
                    return dataSourceBean;
                }
        );
    }

    private static Connection connection(String jdbcUrl) throws SQLException {
        return THREAD_CONNECTION
                .computeIfAbsent(Thread.currentThread().threadId(),
                        key -> {
                            try {
                                return new HashMap<>(Map.of(jdbcUrl, dataSource(jdbcUrl).getConnection()));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .computeIfAbsent(jdbcUrl,
                        key -> {
                            try {
                                return dataSource(jdbcUrl).getConnection();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    public static void closeConnections() {
        for (Map<String, Connection> connections : THREAD_CONNECTION.values()) {
            for (Connection connection : connections.values()) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    //
                }
            }
        }
    }
}
