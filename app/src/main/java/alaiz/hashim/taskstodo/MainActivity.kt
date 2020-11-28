package alaiz.hashim.taskstodo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_to_do.*
import java.util.*

lateinit var tabLayout:TabLayout
lateinit var tabViewPager:ViewPager2


class MainActivity : AppCompatActivity(),ToDoFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout=findViewById(R.id.tabs)
        tabViewPager=findViewById(R.id.pager)




        tabViewPager.adapter=object :FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {


                //tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
               // tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_audiotrack_24)
               // tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_favorite_24)
                tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        Toast.makeText(this@MainActivity , "the number of current page is: "+tab?.position.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        // Handle tab reselect
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        // Handle tab unselect
                    }
                })

                return when (position) {
                    0 -> {
                        ToDoFragment.newInstance()

                    }
                    1 -> {
                        InProgressFragment.newInstance()
                    }
                    2 -> {
                        DoneFragment.newInstance()
                    }
                    else -> {
                        DoneFragment.newInstance()
                    }
                }
            }
            override fun getItemCount(): Int {
                return 3
            }

        }

        TabLayoutMediator(tabLayout, tabViewPager){tab,position->
            tab.text=when(position){
                0 -> "TODO"
                1 ->"In progress"
                2 ->"Done"
                else -> null
            }

        }.attach()

    }

    override fun onTaskSelected(taskId: UUID) {

        val fragment = AddTaskFragment.newInstance(taskId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


}