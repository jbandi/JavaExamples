package net.jonasbandi;

import org.junit.*;

import static org.junit.Assert.assertTrue;

public class HelloBeanTest {

    public HelloBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sayHello method, of class HelloBean.
     */
    @Test @Ignore
    public void testSayHello_String() throws Exception {
        HelloBean b = new HelloBean();
        assertTrue(b.sayHello().equals("Hello"));
    }
}
