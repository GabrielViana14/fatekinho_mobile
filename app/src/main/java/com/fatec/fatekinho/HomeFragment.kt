package com.fatec.fatekinho

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fatec.fatekinho.models.HistWonLose
import com.fatec.fatekinho.models.Usuarios
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var barChart: BarChart
    private lateinit var barChart_hist: BarChart
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        barChart = view.findViewById(R.id.barChart)
        barChart_hist = view.findViewById(R.id.barChart_histMes)
        pieChart = view.findViewById(R.id.pieChart)


        fetchUsuarios() // Busca os usuários para processar os dados
        fetchHistWonLose()
        fetchHistorico()

        return view
    }

    private fun fetchHistWonLose() {
        RetroFitInstance.api.getAllHistWonLose().enqueue(object : Callback<List<HistWonLose>>{
            override fun onResponse(
                call: Call<List<HistWonLose>>,
                response: Response<List<HistWonLose>>
            ) {
                if (response.isSuccessful) {
                    val historico = response.body()
                    if (historico != null){
                        processarDadosHistWonLose(historico)
                    }
                }
            }

            override fun onFailure(call: Call<List<HistWonLose>>, t: Throwable) {
                Log.e("Erro", "Falha ao carregar histórico de vencedores e perdedores", t)
            }

        })
    }

    private fun processarDadosHistWonLose(historico: List<HistWonLose>) {
        val groupedData = mutableMapOf<String, Float>()

        // Agrupando os valores por mês
        historico.forEach { item ->
            val mes = formatarDataParaMesAbreviado(item.dataCadastro) // Formata a data para "MMM"
            groupedData[mes] = groupedData.getOrDefault(mes, 0f) + item.valorApostado.toFloat()



        }

        // Preparando os dados para o gráfico
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var index = 0f

        groupedData.forEach { (mes, valor) ->
            entries.add(BarEntry(index++, valor.toInt().toFloat())) // Adiciona os valores agrupados
            labels.add(mes) // Adiciona os meses
            // Exibe o mês e o valor apostado no log
            Log.e("TAG", "Mês : $mes, Valor Apostado: $valor")
        }

        val barDataSet = BarDataSet(entries, "Histórico de Vencedores e Perdedores")
        barDataSet.color = resources.getColor(R.color.dark_orange)

        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize = 18.0F





        // Configurando as labels no eixo X
        val xAxis = barChart_hist.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        val data = BarData(barDataSet)
        barChart_hist.data = data

        // Atualiza o gráfico
        barChart_hist.invalidate()

        // Configura o gráfico
        configGraficoBarra(barChart_hist)
    }



    private fun fetchUsuarios() {
        // Usando a instância do Retrofit configurada
        RetroFitInstance.api.getAllUsuarios().enqueue(object : Callback<List<Usuarios>> {
            override fun onResponse(call: Call<List<Usuarios>>, response: Response<List<Usuarios>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body()
                    if (usuarios != null) {
                        processarDadosUsuarios(usuarios)
                    }
                }
            }

            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable) {
                // Tratar falhas
            }
        })
    }

    private fun fetchHistorico(){
        RetroFitInstance.api.getAllHistWonLose().enqueue(object : Callback<List<HistWonLose>>{
            override fun onResponse(
                call: Call<List<HistWonLose>>,
                response: Response<List<HistWonLose>>
            ) {
                val jogos = response.body()
                if(jogos != null){
                    processarWinLose(jogos)
                }




            }

            override fun onFailure(call: Call<List<HistWonLose>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun processarWinLose(jogos: List<HistWonLose>) {
        var totalGanho = 0f
        var totalPerdido = 0f

        jogos.forEach { jogo ->
            if (jogo.ganhou == 1) {
                totalGanho += jogo.valorApostado
            } else {
                totalPerdido += jogo.valorApostado
            }
        }

        // Criar dados para o gráfico
        val pieEntries = ArrayList<PieEntry>()
        pieEntries.add(PieEntry(totalGanho, "Ganho"))
        pieEntries.add(PieEntry(totalPerdido, "Perdido"))

        // Configurar o dataset
        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 18f
        dataSet.valueTextColor = Color.RED

        // Criar PieData
        val data = PieData(dataSet)
        pieChart.data = data

        // Personalizar o gráfico
        pieChart.description.isEnabled = false

        // Personalizar as Legendas
        val legend = pieChart.legend
        legend.isEnabled = true // Habilitar a legenda (se estiver desabilitada)
        legend.textColor = android.graphics.Color.WHITE // Cor do texto da legenda
        legend.textSize = 12f // Tamanho do texto da legenda

        // Mudar a cor e o tamanho do texto das entradas
        pieChart.setEntryLabelColor(android.graphics.Color.RED) // Cor do texto (exemplo: vermelho)
        pieChart.setEntryLabelTextSize(18f) // Tamanho do texto (exemplo: 18dp)

        pieChart.animateY(1400) // Animação
    }


    private fun processarDadosUsuarios(usuarios: List<Usuarios>) {
        val tipoCount = mutableMapOf<String, Int>()

        // Contabilizando a quantidade de usuários por tipo
        usuarios.forEach {
            tipoCount[it.tipo] = tipoCount.getOrDefault(it.tipo, 0) + 1
        }

        // Preparando os dados para o gráfico
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var index = 0f

        // Adicionando as entradas ao gráfico
        tipoCount.forEach { (tipo, count) ->
            entries.add(BarEntry(index, count.toFloat()))
            labels.add(tipo)
            index++
        }


        // Convertendo o Set de labels para uma lista para ser usada no ValueFormatter
        val labelsList = labels.toList()

        // Criando o dataset
        val dataSet = BarDataSet(entries, "Usuários por Tipo")



        // Definindo cores alternadas para as barras
        val colors = ArrayList<Int>()
        for (i in entries.indices) {
            if (i % 2 == 0) {
                colors.add(Color.parseColor("#6D62F7"))  // Cor para barras de índice par
            } else {
                colors.add(Color.parseColor("#3DD34C"))  // Cor para barras de índice ímpar
            }
        }
        dataSet.colors = colors
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 18.0F

        // Configurando as labels no eixo X
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)


        // Adiciona os dados ao gráfico
        val barData = BarData(dataSet)
        barChart.data = barData



        // Atualiza o gráfico
        barChart.invalidate()

        // Configurações do visual do gráfico
        configGraficoBarra(barChart)
    }

    private fun configGraficoBarra(barChart: BarChart) {
        // Define a cor dos textos dos eixos
        barChart.xAxis.textColor = Color.WHITE // Cor do texto no eixo X
        barChart.axisLeft.textColor = Color.GREEN // Cor do texto no eixo Y (esquerdo)
        barChart.axisRight.textColor = Color.CYAN // Cor do texto no eixo Y (direito)

        // Remover rótulos dos eixos
        barChart.axisLeft.isEnabled = false // Desabilita os rótulos no eixo Y esquerdo
        barChart.axisRight.isEnabled = false // Desabilita os rótulos no eixo Y direito

        // Configuração básica do gráfico
        barChart.setDrawGridBackground(false)  // Desabilita o fundo da grade
        barChart.setDrawBarShadow(false)       // Desabilita a sombra das barras
        barChart.setDrawBorders(false)         // Desabilita as bordas
        barChart.setTouchEnabled(false)         // Permite interações com o gráfico

        // ajustando posição do labels do xAxis
        barChart.xAxis.granularity = 1f
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawLabels(true)
        barChart.xAxis.setCenterAxisLabels(false)

        // Ajustando o espaçamento entre as barras
        barChart.barData?.barWidth = 0.7f // Ajuste o tamanho das barras para que fiquem mais próximas

        // Desativar as legendas
        barChart.legend.isEnabled = false


        // Remover a descrição do gráfico
        val description = Description()
        description.text = "" // Remover a descrição
        barChart.description = description

        barChart.xAxis.textSize = 16f

        barChart.setExtraOffsets(0f, 0f, 0f, 20f) // Adiciona espaço inferior


        // Configuração de animação
        barChart.animateY(1000)

    }

    private fun processarPieDadosUsuarios(usuarios: List<Usuarios>) {
        val tipoCount = mutableMapOf<String, Int>()

        // Contabilizando a quantidade de usuários por tipo
        usuarios.forEach {
            tipoCount[it.tipo] = tipoCount.getOrDefault(it.tipo, 0) + 1
        }

        // Preparando os dados para o gráfico de pizza
        val entries = ArrayList<PieEntry>()
        val labels = ArrayList<String>()

        // Adicionando as entradas ao gráfico de pizza
        tipoCount.forEach { (tipo, count) ->
            entries.add(PieEntry(count.toFloat(), tipo))  // Cada tipo de usuário se torna uma entrada no gráfico de pizza
            labels.add(tipo)
        }

        // Criando o dataset para o gráfico de pizza
        val dataSet = PieDataSet(entries, "Usuários por Tipo")
        // Definindo cores alternativas para as fatias do gráfico
        val colors = ArrayList<Int>()
        for (i in entries.indices) {
            if (i % 2 == 0) {
                colors.add(Color.parseColor("#6D62F7"))  // Cor para fatias de índice par
            } else {
                colors.add(Color.parseColor("#3DD34C"))  // Cor para fatias de índice ímpar
            }
        }
        dataSet.colors = colors
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 18.0F

        // Criando a PieData com o dataset
        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Atualiza o gráfico
        pieChart.invalidate()

        // Configurações do visual do gráfico de pizza
        configGraficoPizza(pieChart)
    }

    private fun configGraficoPizza(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)  // Exibir os valores em porcentagem
        pieChart.description.isEnabled = false  // Desabilitar a descrição do gráfico
        pieChart.isRotationEnabled = true  // Habilitar a rotação do gráfico
        pieChart.setDrawEntryLabels(false)  // Desabilitar rótulos nas fatias
        pieChart.legend.isEnabled = true  // Habilitar legenda

        // Configurando o texto da legenda
        val legend = pieChart.legend
        legend.textSize = 14f  // Define o tamanho do texto (em dp)
        legend.textColor = Color.WHITE  // Define a cor do texto (exemplo de cor branca)
    }



    // Função para formatar a data
    private fun formatarDataParaMesAbreviado(dataCadastro: String): String {
        val formatosEntrada = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
        val formatoSaida = SimpleDateFormat("MMM", Locale("pt", "BR"))

        for (formato in formatosEntrada) {
            try {
                val formatoEntrada = SimpleDateFormat(formato, Locale.getDefault())
                val data = formatoEntrada.parse(dataCadastro)
                if (data != null) {
                    return formatoSaida.format(data)
                }
            } catch (e: Exception) {
                // Tenta o próximo formato
            }
        }
        return "Inválido" // Retorna "Inválido" se nenhum formato corresponder
    }


}
