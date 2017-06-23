package io.github.legosteen11.simpledbclient.client

import io.github.legosteen11.simpledbclient.IDatabaseClient
import org.h2.tools.Server
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import org.junit.Assert.assertEquals

/**
 * Created by wouter on 6/23/17.
 */
internal class SimpleDatabaseClientTest {
    lateinit var server: Server
    lateinit var client: IDatabaseClient

    @Before
    fun setup() {
        // setup H2
        server = Server.createTcpServer().start()

        client = SimpleDatabaseClient("", "", "", "", jdbcUrl = "jdbc:h2:tcp://localhost/~/test")

        client.executeUpdate("CREATE TABLE IF NOT EXISTS public.test(id INT, name VARCHAR(50))")
    }

    @Test
    fun getConnection() {
        assert(client.getConnection() is Connection)
    }

    @Test
    fun executeUpdate() {
        assertEquals(0, client.executeUpdate("CREATE TABLE IF NOT EXISTS public.test(id INT, name VARCHAR(50))"))
        assertEquals(1, client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 5, "testname"))
        assertEquals(1, client.executeUpdate("INSERT INTO public.test VALUES(?, ?)", 17, "testname2"))
    }

    @Test
    fun executeQuery() {
        assertEquals("testname", client.executeQuery("SELECT name FROM public.test WHERE id = ?", 5).apply { next() }.getString("name"))
        assertEquals(null, client.getSingleValueQuery("SELECT name FROM public.test WHERE id = ?", 20))
        assertEquals("testname2", client.getSingleValueQuery("SELECT name FROM public.test WHERE id = ?", 17))
    }

    @After
    fun cleanup() {
        server.stop()
    }
}