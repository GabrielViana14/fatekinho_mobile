import android.os.AsyncTask
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseHelper(
    private val url: String,
    private val user: String,
    private val password: String
) {

    fun connect(callback: (String) -> Unit) {
        ConnectToDatabase(callback).execute()
    }

    private inner class ConnectToDatabase(private val callback: (String) -> Unit) :
        AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String? {
            var connection: Connection? = null
            return try {
                // Carregar o driver JDBC
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")

                // Estabelecer a conexão
                connection = DriverManager.getConnection(url, user, password)
                "Conexão estabelecida com sucesso!"
            } catch (e: SQLException) {
                e.printStackTrace()
                "Erro ao conectar: ${e.message}"
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                "Driver não encontrado: ${e.message}"
            } finally {
                connection?.close()
            }
        }

        override fun onPostExecute(result: String?) {
            callback(result ?: "Erro desconhecido")
        }
    }
}
