package io.github.legosteen11.simpledbclient.client

import io.github.legosteen11.simpledbclient.IDatabaseClient
import org.h2.tools.Server
import org.junit.*
import java.sql.Connection
import java.sql.Statement


/**
 * Base test class.
 *
 * To create a test class for your own DatabaseClient extend this class and implement the getClientInstance() function that returns an instance of your class.
 */
abstract class DatabaseClientTestBase<out T : IDatabaseClient> {
    private lateinit var server: Server
    protected val jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    protected val host = ""
    protected val username = ""
    protected val password = ""
    protected val database = ""
    private lateinit var client: T

    @Before
    fun setup() {
        // setup H2
        server = Server.createTcpServer().start()

        client = getClientInstance()

        //client.executeUpdate("DROP ALL OBJECTS [DELETE FILES]")

//        client.executeUpdate("TRUNCATE TABLE public.test")
//        client.executeUpdate("TRUNCATE TABLE public.test2")

        client.executeUpdate("CREATE TABLE IF NOT EXISTS public.test(id INT, name VARCHAR(50))")
        client.executeUpdate("CREATE TABLE IF NOT EXISTS public.test2(id INT AUTO_INCREMENT, name VARCHAR(50))")
    }

    @Test
    fun getConnection() {
        assert(client.getConnection() is Connection)
    }

    @Test
    fun executeUpdate() {
        Assert.assertEquals(1, client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 5, "testname"))
        Assert.assertEquals(1, client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 17, "testname2"))
        Assert.assertEquals(1, client.executeUpdate("INSERT INTO public.test2(name) VALUES(?)","autogenkeystest", returnGeneratedKeys = Statement.RETURN_GENERATED_KEYS))
        Assert.assertEquals(2, client.executeUpdate("INSERT INTO public.test2(name) VALUES(?)","autogenkeystest2", returnGeneratedKeys = Statement.RETURN_GENERATED_KEYS))
        Assert.assertEquals(3, client.executeUpdate("INSERT INTO public.test2(name) VALUES(?)","autogenkeystest3", returnGeneratedKeys = Statement.RETURN_GENERATED_KEYS))
    }

    @Test
    fun executeQuery() {
        client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 5, "testname")

        Assert.assertEquals("testname", client.executeQuery("SELECT name FROM public.test WHERE id = ?", 5).apply { next() }.getString("name"))
    }

    @Test
    fun getSingleValueQuery() {
        client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 17, "testname2")

        Assert.assertEquals(null, client.getSingleValueQuery("SELECT name FROM public.test WHERE id = ?", 20))
        Assert.assertEquals("testname2", client.getSingleValueQuery("SELECT name FROM public.test WHERE id = ?", 17))
    }

    @Test
    fun executeBatchUpdate() {
        Assert.assertEquals(arrayOf(1, 2,  3).joinToString(), client.executeBatchUpdate("INSERT INTO public.test2(name) VALUES(?)", arrayOf("test1"), arrayOf("test2"), arrayOf("Test3"), returnGeneratedKeys = Statement.RETURN_GENERATED_KEYS).joinToString())
        Assert.assertEquals(arrayOf(1, 1, 1).joinToString(), client.executeBatchUpdate("INSERT INTO public.test VALUES(?, ?)", arrayOf(1, "test1"), arrayOf(3, "test2"), arrayOf(314, "test3")).joinToString())
    }

    abstract fun getClientInstance(): T

    @After
    fun tearDown() {
        client.executeUpdate("SHUTDOWN")

        client.disconnect()
    }
}