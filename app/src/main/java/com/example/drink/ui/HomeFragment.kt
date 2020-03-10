package com.example.drink.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drink.R
import com.example.drink.data.model.Drink
import com.example.drink.ui.adapter.DrinkListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)

            val adapter = DrinkListAdapter(it)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)

            adapter.onItemClick = {
                if (adapter.selecteds.isNotEmpty()) {
                    this.fab_delete.show()
                    this.fab_new.hide()
                } else {
                    this.fab_new.show()
                    this.fab_delete.hide()
                }
            }

            adapter.onItemLongClick = { drink ->
                this.HandleDrinkPrompt(drink)
            }

            this.fab_new.setOnClickListener {
                this.HandleDrinkPrompt(null)
            }

            this.fab_delete.setOnClickListener {
                adapter.selecteds.forEach { drink ->
                    viewModel.delete(drink)
                }
                this.fab_new.show()
                this.fab_delete.hide()
            }

            viewModel.drinks.observe(this, Observer { drinks ->
                viewModel.profiles.observe(this, Observer { profiles ->
                    profiles?.let {
                        drinks?.let {
                            val profile = profiles.first()
                            this.text_dailyAmount.text = viewModel.getDailyAmountText(profile)
                            this.text_dailyGoalAmount.text =
                                viewModel.getDailyGoalAmountText(profile)
                            this.text_dailyLeftAmount.text =
                                viewModel.getDailyLeftAmountText(profile)
                            this.circular_progressbar.progress = viewModel.getProgress(profile)
                            adapter.setDrinks(
                                viewModel.getTodayDrinks(drinks),
                                profile.isUnitInKg()
                            )
                        }
                    }
                })
            })
        }
    }

    private fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    private fun HandleDrinkPrompt(drink: Drink?) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.drink_prompt, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(promptsView)

        val userInput = promptsView.findViewById<View>(R.id.drink_prompt_input) as EditText
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                viewModel.profiles.observeOnce(Observer { profiles ->
                    val input = userInput.text.toString()
                    if (input.isNotEmpty() && input.toIntOrNull() != null) {
                        val profile = profiles.first()
                        val storeAmount =
                            viewModel.getStoreAmount(profile, userInput.text.toString().toInt())
                        if (drink == null) {
                            viewModel.insert(Drink(0, profile.id, storeAmount, Date().time))
                        } else {
                            viewModel.update(Drink(drink.id, profile.id, storeAmount, drink.date))
                        }
                    }
                })
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}