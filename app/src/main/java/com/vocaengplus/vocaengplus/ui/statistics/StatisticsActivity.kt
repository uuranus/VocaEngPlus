package com.vocaengplus.vocaengplus.ui.statistics

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityStatisticsBinding
import com.vocaengplus.vocaengplus.databinding.StatisticshelpBinding
import com.vocaengplus.vocaengplus.di.Initialization
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class StatisticsActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticsBinding
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var firebaseUser: FirebaseUser
    lateinit var uid:String
    val initialization= Initialization
    lateinit var d_date:String
    var mon=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.helpButton.setOnClickListener {
            val dlgBinding= StatisticshelpBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(this)
            builder.setView(dlgBinding.root)
                    .setNeutralButton("확인"){
                        _,_ ->{

                    }
                    }
            val dlg=builder.create()
            dlg.show()
        }



        binding.preMonthButton.setOnClickListener {
            mon--
            checkClickable()
            init()
        }
        binding.nextMonthButton.setOnClickListener {
            mon++
            checkClickable()
            init()
        }

        checkClickable()
        init()

    }

    private fun checkClickable(){
        if(mon==-2){
            binding.preMonthButton.isClickable=false
            binding.preMonthButton.isActivated=false
            binding.nextMonthButton.isClickable=true
            binding.nextMonthButton.isActivated=true
        }
        else if(mon==-1){
            binding.preMonthButton.isClickable=true
            binding.preMonthButton.isActivated=true
            binding.nextMonthButton.isClickable=true
            binding.nextMonthButton.isActivated=true
        }
        else if(mon==0){
            binding.preMonthButton.isClickable=true
            binding.preMonthButton.isActivated=true
            binding.nextMonthButton.isClickable=false
            binding.nextMonthButton.isActivated=false
        }
    }

    private fun init(){
        firebaseauth= initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        databaseref=initialization.getDBref()
        uid=initialization.getuid()
        d_date=getdate(mon)

        binding.dateTextView.text="${d_date.substring(0,4)}년 ${d_date.substring(5,7)}월 통계"

        databaseref.child("UserLog").child(uid).get().addOnSuccessListener {snapshot->
            if(snapshot.hasChild(d_date.substring(0,7))){
                binding.apply{
                    totalTestTextView.text="총 테스트한 수 : ${snapshot.child(d_date.substring(0,7)).child("Test").child("testcount").value.toString()}번"
                    val tc=snapshot.child(d_date.substring(0,7)).child("Test").child("totalcount").value.toString().toInt()
                    totalCountTextView.text="총 테스트한 단어 수 :  ${tc}개"
                    val cc=snapshot.child(d_date.substring(0,7)).child("Test").child("corcount").value.toString().toInt()
                    correctCountTextView.text="총 맞은 단어 수 :  ${cc}개"
                    wrongCountTextView.text="총 틀린 단어 수 :  ${snapshot.child(d_date.substring(0,7)).child("Test").child("wrongcount").value.toString()}개"
                    var cp=0f
                    if(tc!=0){
                        cp= (((cc / tc.toFloat()) * 10000).roundToInt().toFloat() /100)
                    }
                    correctPercentTextView.text="정답률 :  ${cp}%"


                    val nc=snapshot.child(d_date.substring(0,7)).child("AddDelete").child("addCategory").value.toString().toInt()
                    newCategoryTextView.text="새로 추가한 단어장 :  ${nc}개"
                    val nw=snapshot.child(d_date.substring(0,7)).child("AddDelete").child("addWord").value.toString().toInt()
                    newWordTextView.text="새로 추가한 단어 :  ${nw}개"
                    val dc=snapshot.child(d_date.substring(0,7)).child("AddDelete").child("deleteCategory").value.toString().toInt()
                    deleteCategoryTextView.text="새로 삭제한 단어장 :  ${dc}개"
                    val dw=snapshot.child(d_date.substring(0,7)).child("AddDelete").child("deleteWord").value.toString().toInt()
                    deleteWordTextView.text="새로 삭제한 단어 :  ${dw}개"
                    var ap=0f
                    if((nc+nw+dc+dw)!=0){
                       ap=((((nc + nw) / (nc + nw + dc + dw).toFloat()) * 10000).roundToInt().toFloat() /100)
                    }
                    addPercentTextView.text="추가율 :  ${ap}%"

                }
                initChart() //데이터 가져오고 차트 초기화 (initData 를 하는 경우 차트 진행이 먼저 되면 안되기 때문)
            }
            else{
                //해당 월의 내용 초기화
                initData()
            }
        }


    }

    private fun initChart(){
        val testdata=ArrayList<Entry>()
        val percentdata2=ArrayList<BarEntry>()
        val percentdata=ArrayList<BarEntry>()


        databaseref.child("UserLog").child(uid.toString()).get().addOnSuccessListener{snapshot->
            var test=snapshot.child(d_date.substring(0,7)).child("Test")
            //해당 달 데이터 초기화
            val lastdate=getLastDate(mon)

            if(test.hasChild("log")) {
                if(mon==0){
                    for(i in 0 until d_date.substring(8,10).toInt()){
                        if(test.child("log").hasChild(d_date.substring(0,8)+String.format("%02d",i+1))){
                            testdata.add(Entry((i+1).toFloat(), test.child("log").child(d_date.substring(0,8)+String.format("%02d",i+1)).child("testcount").value.toString().toFloat()))
                            val tc = test.child("log").child(d_date.substring(0,8)+String.format("%02d",i+1)).child("totalcount").value.toString().toFloat()
                            val cc = test.child("log").child(d_date.substring(0,8)+String.format("%02d",i+1)).child("corcount").value.toString().toFloat()
                            var cp = 0f
                            if (tc != 0f) {
                                cp = (((cc / tc.toFloat()) * 10000).roundToInt().toFloat() /100)
                            }
                            percentdata.add(BarEntry((i+1).toFloat(), cp))
                        }
                        else{
                            testdata.add(Entry((i+1).toFloat(), 0f))
                            percentdata2.add(BarEntry((i+1).toFloat(), 0f))
                            percentdata.add(BarEntry((i+1).toFloat(), 0f))
                        }
                    }
                }
                else{
                    for(i in 0 until lastdate){
                        if(test.child("log").hasChild(d_date.substring(0,8)+String.format("%02d",i+1))){
                            testdata.add(Entry((i+1).toFloat(), test.child("log").child(d_date.substring(0,8)+String.format("%02d",i)).child("testcount").value.toString().toFloat()))
                            val tc = test.child("log").child(d_date.substring(0,8)+String.format("%02d",i+1)).child("totalcount").value.toString().toFloat()
                            val cc = test.child("log").child(d_date.substring(0,8)+String.format("%02d",i+1)).child("corcount").value.toString().toFloat()
                            var cp = 0f
                            if (tc != 0f) {
                                cp = (((cc / tc.toFloat()) * 10000).roundToInt().toFloat() /100)
                            }
                            percentdata.add(BarEntry((i+1).toFloat(), cp))
                        }
                        else{
                            testdata.add(Entry((i+1).toFloat(), 0f))
                            percentdata2.add(BarEntry((i+1).toFloat(), 0f))
                            percentdata.add(BarEntry((i+1).toFloat(), 0f))
                        }
                    }
                }
            }
            else{
                if(mon==0){
                    for(i in 0 until d_date.substring(8,10).toInt()){
                        testdata.add(Entry((i+1).toFloat(), 0f))
                        percentdata2.add(BarEntry((i+1).toFloat(), 0f))
                        percentdata.add(BarEntry((i+1).toFloat(), 0f))
                    }
                }
                else{
                    for(i in 0 until lastdate){
                        testdata.add(Entry((i+1).toFloat(), 0f))
                        percentdata.add(BarEntry((i+1).toFloat(), 0f))
                        percentdata2.add(BarEntry((i+1).toFloat(), 0f))
                    }
                }
            }
            setChart(testdata,percentdata2,percentdata)
        }

    }

    private fun setChart(testdata:ArrayList<Entry>, percentdata2:ArrayList<BarEntry>,percentdata:ArrayList<BarEntry>){

        val linepercentdata=LineDataSet(testdata,"테스트 횟수")
        linepercentdata.setColor(ContextCompat.getColor(this, R.color.darkblue))
        linepercentdata.setCircleColor(ContextCompat.getColor(this, R.color.darkblue))
        linepercentdata.valueTextSize=8f
        linepercentdata.valueTextColor= R.color.darkblue
        linepercentdata.setDrawValues(true)
        linepercentdata.circleHoleColor= R.color.darkblue
        linepercentdata.axisDependency=YAxis.AxisDependency.RIGHT

        val linedata=LineData(linepercentdata)

        val bartestdata=BarDataSet(percentdata,"정답률")
        bartestdata.setColor(ContextCompat.getColor(this, R.color.lightblue))
        bartestdata.valueTextSize=8f
        bartestdata.valueTextColor= R.color.lightblue
        bartestdata.axisDependency=YAxis.AxisDependency.LEFT

        val bartestdata2=BarDataSet(percentdata2,"")
        bartestdata2.setColor(ContextCompat.getColor(this, R.color.white))
        bartestdata2.setDrawValues(false)
        bartestdata2.setDrawIcons(false)
        bartestdata2.axisDependency=YAxis.AxisDependency.LEFT

        val bardata=BarData(bartestdata,bartestdata2)
        bardata.barWidth=0.5f

        var data=CombinedData()
        data.setData(bardata)
        data.setData(linedata)

        val xAxis= binding.chart.xAxis //x축 가져오기

        xAxis.apply{
            position= XAxis.XAxisPosition.BOTTOM  //x축 데이터는 아래에 위치
            textColor=Color.BLACK
            textSize=10f //텍스트 크기 지정(float 형!)
            setDrawGridLines(true) //배경 그리드 라인 세팅
            granularity=1f //x축 데이터 표시 간격
            axisMinimum=1f //x축 데이터 최소 표시값
            axisMaximum=31f //x축 데이터 최대 표시값 -
            isGranularityEnabled=true //x축 간격을 제한하는 세분화 기능
        }

        val yAxisLeft= binding.chart.axisLeft //왼족 y축 가져오기 테스트, 정답률 같이 사용
        yAxisLeft.apply{
            textColor=Color.BLACK
            granularity=1f //y축 데이터 표시 간격
            axisMaximum=100f
            axisMinimum=0f
        }

        val yAxisRight= binding.chart.axisRight //오른쪽 y축 비활성화
        yAxisRight.apply{
            textColor= Color.BLACK
            granularity=1f //y축 데이터 표시 간격
            axisMaximum=20f
            axisMinimum=0f
        }

        binding.chart.apply{ //차트 세팅
            legend.apply {  //범례 세팅
                textSize=10f //텍스트 크기(float 형)
                verticalAlignment= Legend.LegendVerticalAlignment.BOTTOM //수직 조정 -> 위로
                horizontalAlignment=Legend.LegendHorizontalAlignment.LEFT //수평 조정 -> 가운데
                orientation=Legend.LegendOrientation.HORIZONTAL //범례와 차트 정렬 -> 수평
                setDrawInside(false) //차트 안에 그릴 것인가?
                xEntrySpace=20f
            }
            description.isEnabled=false
            setDrawBarShadow(false)
            isHighlightFullBarEnabled=false
        }


        binding.chart.data=data
        binding.chart.drawOrder= arrayOf<CombinedChart.DrawOrder>(CombinedChart.DrawOrder.BAR,CombinedChart.DrawOrder.LINE) //바 위에 꺽은 선 그래프 그리기기
        binding.chart.notifyDataSetChanged()
        binding.chart.invalidate()
    }

    private fun getdate(month:Int) : String{
        val mdate=Calendar.getInstance()
        when(month){
            0 ->{

            }
            -1 ->{
                mdate.add(Calendar.MONTH,-1)
            }
            -2 ->{
                mdate.add(Calendar.MONTH,-2)
            }
        }
        val simpelDate : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val getTime : String = simpelDate.format(mdate.time)
        return getTime

    }

    private fun getLastDate(month:Int) : Int{
        val mdate=Calendar.getInstance()
        mdate.add(Calendar.MONTH,mon)
        val lastdate=mdate.getActualMaximum(Calendar.DAY_OF_MONTH)
        return lastdate
    }

    private fun initData(){
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("Test").child("testcount").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("Test").child("totalcount").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("Test").child("corcount").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("Test").child("wrongcount").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("AddDelete").child("addCategory").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("AddDelete").child("addWord").setValue(0)
            Initialization.databaseref.child("UserLog").child(Initialization.uid.toString()).child(d_date.substring(0,7)).child("AddDelete").child("deleteWord").setValue(0)

            init() //데이터 다시 가져오기
    }

}