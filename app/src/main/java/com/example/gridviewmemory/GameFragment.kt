package com.example.gridviewmemory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.gridviewmemory.R.drawable.*
import com.example.gridviewmemory.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    var max = 0
    var moves = MutableLiveData<Int>()
    private lateinit var cards : List<MemoryCard>
    var matches = 0
    var selectedCard : Int? = null
    lateinit var img_View : ImageView
    lateinit var binding : FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)

        val level = arguments?.let { GameFragmentArgs.fromBundle(it) }?.level

        var images = mutableListOf(ic_baseline_bike_scooter_24,
            ic_baseline_sentiment_very_satisfied_24, ic_baseline_weekend_24, ic_baseline_wine_bar_24,
            ic_baseline_smoke_free_24, ic_baseline_task_24)


        if (level==0){
            max = 11
            moves.value = 10
            matches=6
            images.addAll(images)
        }
        else if (level==1){
            max = 17
            moves.value = 17
            matches=9
            images = mutableListOf(ic_baseline_bike_scooter_24,
                ic_baseline_sentiment_very_satisfied_24, ic_baseline_weekend_24, ic_baseline_wine_bar_24,
                ic_baseline_smoke_free_24, ic_baseline_task_24,
                ic_baseline_tips_and_updates_24,
                ic_baseline_warning_24,
                ic_baseline_thumb_up_24)
            images.addAll(images)
        }else{
            max = 23
            moves.value = 24
            matches=12
            images = mutableListOf(ic_baseline_sentiment_very_satisfied_24, ic_baseline_weekend_24, ic_baseline_wine_bar_24,
                ic_baseline_smoke_free_24, ic_baseline_task_24, ic_baseline_tips_and_updates_24,
                ic_baseline_warning_24, ic_baseline_thumb_up_24,ic_baseline_bike_scooter_24,
                ic_baseline_water_drop_24, ic_baseline_watch_later_24, ic_baseline_child_care_24)
            images.addAll(images)
           // binding.gridView.numColumns = 4
        }

        images.shuffle()

        cards = images.indices.map{
            i->
            MemoryCard(images[i])
        }

        val adapter = MyGridViewAdapter(requireNotNull(this.activity).application)

        adapter.setDrawables(images)

        binding.gridView.adapter = adapter

        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                updateCard(position)
                img_View= view as ImageView
                updateView()
            }

        return binding.root
    }

    fun updateView(){
        cards.forEachIndexed { index, card ->
            val button : ImageView = binding.gridView.get(index) as ImageView
            if(card.matched){
                button.alpha=0.25F
            }
            button.setImageResource(if (card.face_up) card.identity else ic_android_black_24dp)
        }
    }

    fun updateCard(index : Int){
        val card = cards[index]
        if (card.face_up) {
            Toast.makeText(this.context, "Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedCard==null){
            reset()
            selectedCard=index
        }else{
            checkMatch(selectedCard!!,index)
            moves.value=moves.value?.minus(1)

            if(matches==10){
                this.findNavController().navigate(GameFragmentDirections.actionGameFragmentToWinFragment())
            }
            else if (moves.value!! == 0){
                this.findNavController().navigate(GameFragmentDirections.actionGameFragmentToLoseFragment())
            }
            selectedCard=null
        }
        card.face_up = !card.face_up
    }

    private fun reset() {
        for(card in cards){
            if(!card.matched){
                card.face_up=false
            }
            updateView()
        }
    }

    private fun checkMatch(past:Int,present:Int) {
        if (cards[past].identity==cards[present].identity){
            Toast.makeText(this.context,"MATCH FOUND",Toast.LENGTH_SHORT).show()
            matches++
            cards[past].matched=true
            cards[present].matched=true
        }
    }
}