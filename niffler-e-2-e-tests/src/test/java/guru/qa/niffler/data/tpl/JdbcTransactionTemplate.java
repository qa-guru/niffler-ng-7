package guru.qa.niffler.data.tpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class JdbcTransactionTemplate {

    private final JdbcConnectionHolder holder;
    private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

    public JdbcTransactionTemplate(String jdbcUrl) {
        this.holder = Connections.holder(jdbcUrl);
    }

    public JdbcTransactionTemplate holdConnectionAfterAction() {
        this.closeAfterAction.set(false);
        return this;
    }


    public <T> T execute(int isolationLevel, Supplier<T> action) {
        Connection connection = null;
        try {
            connection = holder.connection();
            connection.setTransactionIsolation(isolationLevel);
            connection.setAutoCommit(false);
            T result;
            result = action.get();
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    if (closeAfterAction.get()) {
                        holder.close();
                    }
                }
            }
            throw new RuntimeException(e);
        }
    }

    public <T> T execute(Supplier<T> action) {
        return execute(Connection.TRANSACTION_READ_COMMITTED, action);
    }
}
