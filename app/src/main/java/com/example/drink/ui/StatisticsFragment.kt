package com.example.drink.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drink.R
import com.example.drink.data.model.Drink
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            viewModel.drinks.observe(this, Observer { drinks ->
                drinks?.let {
                    onDraw(viewModel.getTodayDrinks(it))
                }
            })
        }
    }

    private fun onDraw(drinks: List<Drink>) {
        viewModel.profiles.observe(this, Observer { profiles ->
            val profile = profiles.first()
            this.graph.removeAllSeries()
            val series = BarGraphSeries<DataPoint>(arrayOf())
            var sum = 0.0
            var counter = 0.0
            drinks
                .sortedBy { t -> t.date }
                .forEach {
                    sum += if (profile.isUnitInKg()) it.getMlAmount() else it.getOzAmount()
                    val point = DataPoint(counter++, sum)
                    series.appendData(point, true, 500)
                }
            series.appendData(DataPoint(counter, 0.0), true, 500)
            series.spacing = 5
            series.isAnimated = true
            graph.addSeries(series)
            graph.viewport.isScalable = true
            graph.viewport.calcCompleteRange()
        })
    }
}