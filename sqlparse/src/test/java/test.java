import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class test {
    public static void main(String[] args) {

        DbType dbType = JdbcConstants.MYSQL;
//        List<SQLStatement> statementList = SQLUtils.parseStatements("id=3", dbType);
        SQLExpr expr = SQLUtils.toSQLExpr("id=3", dbType);
    }
}
