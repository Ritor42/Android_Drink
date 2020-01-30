package com.example.drink.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drink.R
import com.example.drink.data.model.Profile
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            var lastSelected = -1
            val input = this.text_weight_input
            val units = resources.getStringArray(R.array.Units)
            val adapter = ArrayAdapter(
                it,
                R.layout.spinner_item_unit, units
            )
            this.spinner_unit_input.adapter = adapter

            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            viewModel.profiles.observe( this, Observer {  profiles ->
                profiles?.let {
                    if(profiles.isNotEmpty()) {
                        val profile = profiles.first()
                        this.text_age_input.setText(profile.age.toString())
                        this.text_weight_input.setText(viewModel.getProfileWeight(profile).toString())
                        if(profile.isUnitInKg()) {
                            lastSelected = 0
                            this.spinner_unit_input.setSelection(0)
                        } else {
                            lastSelected = 1
                            this.spinner_unit_input.setSelection(1)
                        }
                    }
                }
            })

            this.spinner_unit_input.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(lastSelected != -1 && lastSelected != position) {
                        if (lastSelected == 0) {
                            input.setText(Profile.convertKgToPound(input.text.toString().toInt()).toString())
                        } else {
                            input.setText(Profile.convertPoundToKg(input.text.toString().toInt()).toString())
                        }
                    }
                }
            }

            this.button_save.setOnClickListener {
                val age = this.text_age_input.text.toString().toInt()
                val weight = this.text_weight_input.text.toString().toInt()
                val isUnitInKg = this.spinner_unit_input.selectedItem.toString()
                val profileWeight = viewModel.getStoreWeight(Profile.isUnitInKg(isUnitInKg), weight)

                val profile = Profile(1, age, profileWeight, isUnitInKg)
                viewModel.update(profile)

                this.text_age_input.clearFocus()
                this.text_weight_input.clearFocus()
                this.spinner_unit_input.clearFocus()
                val fr = fragmentManager?.beginTransaction()
                fr?.replace(R.id.nav_host_fragment, HomeFragment())
                fr?.commit()
            }
        }
    }
}