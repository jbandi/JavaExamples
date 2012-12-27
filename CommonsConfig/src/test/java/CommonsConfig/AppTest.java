package CommonsConfig;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception
    {
        XMLConfiguration xmlconfig = new XMLConfiguration("const.xml");

        System.out.println(xmlconfig.getString("database.url"));

        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        Connection conn = ds.getConnection();

        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE myconfig (" +
                "     `key`   VARCHAR NOT NULL PRIMARY KEY," +
                "     `value` VARCHAR" +
                " );" +
                "INSERT INTO myconfig (key, value) VALUES ('foo', 'bar');"
        );


        // Convert a xmlconfig to db
        AbstractConfiguration dbconfig = new DatabaseConfiguration(ds, "myconfig", "key", "value");
        String value = dbconfig.getString("foo");
        System.out.println(value);

        dbconfig.addProperty("foo2", "bar2");

        dbconfig.clear(); // optional
        dbconfig.copy(xmlconfig);

        Statement query = conn.createStatement();
        ResultSet rs = query.executeQuery("select key,value from myconfig");
        while(rs.next()){
            System.out.println(rs.getString(1) + " : " + rs.getString(2));
        }

        // Convert a dbconfig to xml
        XMLConfiguration xmlcofig2 = new XMLConfiguration();
        xmlcofig2.copy(dbconfig);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlcofig2.save(out);
        System.out.println(out.toString());
    }
}
