package io.github.legosteen11.simpledbclient.client

import io.github.legosteen11.simpledbclient.IDatabaseClient
import org.h2.tools.Server
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.sql.Connection

/**
 * Base test class.
 *
 * To create a test class for your own DatabaseClient extend this class and implement the getClientInstance() function that returns an instance of your class.
 */
abstract class DatabaseClientTestBase<out T : IDatabaseClient> {
    private lateinit var server: Server
    protected val jdbcUrl = "jdbc:h2:tcp://localhost/~/test"
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

        client.executeUpdate("CREATE TABLE IF NOT EXISTS public.test(id INT, name VARCHAR(50))")
    }

    @Test
    fun getConnection() {
        assert(client.getConnection() is Connection)
    }

    @Test
    fun executeUpdate() {
        Assert.assertEquals(0, client.executeUpdate("CREATE TABLE IF NOT EXISTS public.test(id INT, name VARCHAR(50))"))
        Assert.assertEquals(1, client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 5, "testname"))
        Assert.assertEquals(1, client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 17, "testname2"))
    }

    @Test
    fun executeQuery() {
        Assert.assertEquals("testname", client.executeQuery("SELECT name FROM public.test WHERE id = ?", 5).apply { next() }.getString("name"))
    }

    @Test
    fun getSingleValueQuery() {
        Assert.assertEquals(null, client.getSingleValueQuery("SELECT name FROM public.test WHERE id = ?", 20))
        Assert.assertEquals("testname2", client.getSingleValueQuery("SELECT name FROM public.test WHERE id = ?", 17))
    }

    @After
    fun cleanup() {
        server.stop()
    }

    abstract fun getClientInstance(): T
}