package guru.qa.niffler.dataBase.dbConnection;




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

public class DataBases {

    private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String, Connection>> treadConnections = new ConcurrentHashMap<>();

    private DataBases() {
    }

    public record XaFunction<T>(Function<Connection,T> function,String jdbcUrl){}
    public record XaConsumer(Consumer<Connection> function,String jdbcUrl){}



    public static <T> T transaction(Function<Connection,T> function,String jdbcUrl,int transactionLevel){
        Connection connection = null;
            try {
                connection = connection(jdbcUrl);
                connection.setTransactionIsolation(transactionLevel);
                connection.setAutoCommit(false);
                T result;
                result = function.apply(connection);
                connection.commit();
                connection.setAutoCommit(true);
                return result;
            } catch (SQLException e) {
                if (connection != null){
                    try {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                throw new RuntimeException(e);
            }
    }

    public static <T> T xaTransaction(int transactionLevel,XaFunction<T>... actions){
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            T result = null;
            for (XaFunction<T> action:actions){
                try{
                    Connection connection = connection(action.jdbcUrl);
                    connection.setAutoCommit(false);
                    connection.setTransactionIsolation(transactionLevel);
                    result = action.function.apply(connection(action.jdbcUrl));
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
            ut.commit();
            return result;
        } catch (Exception e) {
                try {
                  ut.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static void xaTransaction(int transactionLevel,XaConsumer... actions){
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            for (XaConsumer action:actions){
                try{
                    Connection connection = connection(action.jdbcUrl);
                    connection.setAutoCommit(false);
                    connection.setTransactionIsolation(transactionLevel);
                    action.function.accept(connection(action.jdbcUrl));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            ut.commit();
        } catch (Exception e) {
            try {
                ut.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static void transaction(Consumer<Connection> consumer, String jdbcUrl,int transactionLevel){
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setTransactionIsolation(transactionLevel);
            connection.setAutoCommit(false);
            consumer.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null){
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static DataSource dataSource(String jdbcUrl) {
        return datasources.computeIfAbsent(
                jdbcUrl,
                key -> {
                    AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
                    final String uniqId = StringUtils.substringAfter(jdbcUrl,"5432/");
                    dsBean.setUniqueResourceName(uniqId);
                    dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
                    Properties pop = new Properties();
                    pop.put("URL",jdbcUrl);
                    pop.put("user","postgres");
                    pop.put("password","secret");
                    dsBean.setXaProperties(pop);
                    dsBean.setMaxPoolSize(10);
                    return dsBean;
                }
        );
    }

    public static Connection connection(String jdbcUrl) throws SQLException {
        return treadConnections.computeIfAbsent(
                Thread.currentThread().threadId(),
                key -> {
                    try {
                        return new HashMap<>(Map.of(
                                jdbcUrl,
                                dataSource(jdbcUrl).getConnection()
                        ));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).computeIfAbsent(
                jdbcUrl,
                key -> {
                    try {
                        return dataSource(jdbcUrl).getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public static void closeAllConnections() {
        for (Map<String, Connection> connectionMap : treadConnections.values()) {
            for (Connection connection : connectionMap.values()) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {

                }
            }
        }
    }
}
