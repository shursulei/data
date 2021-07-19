package com.shursulei.sqlparse;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsSchemaStatVisitor;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.DruidDataSourceUtils;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class Sqlparsetest {
    private static final Log LOG = LogFactory.getLog(Sqlparsetest.class);
    private static List<SQLStatement> getSQLStatementList(String sql) {
        DbType dbType = JdbcConstants.ODPS;
        return SQLUtils.parseStatements(sql, dbType);
    }

    public static void main(String[] args) {
        String sql = "select * from user order by id;";
        String result = SQLUtils.format(sql, JdbcConstants.ODPS);
        List<SQLStatement> sqlStatementList = getSQLStatementList(sql);
        //默认为一条sql语句
        SQLStatement stmt = sqlStatementList.get(0);
        OdpsSchemaStatVisitor visitor=new OdpsSchemaStatVisitor();
//        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println("数据库类型\t\t" + visitor.getDbType());
        //获取字段名称
        System.out.println("查询的字段\t\t" + visitor.getColumns());
        //获取表名称
        System.out.println("表名\t\t\t" + visitor.getTables().keySet());
        System.out.println("条件\t\t\t" + visitor.getConditions());
        System.out.println("group by\t\t" + visitor.getGroupByColumns());
        System.out.println("order by\t\t" + visitor.getOrderByColumns());


    }
}