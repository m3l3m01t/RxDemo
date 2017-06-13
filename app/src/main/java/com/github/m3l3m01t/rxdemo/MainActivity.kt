package com.github.m3l3m01t.rxdemo

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.github.m3l3m01t.rxdemo.Rest.TestService
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class MainActivity : AppCompatActivity() {
    val service = TestService.create("http://localhost:61737/api/")
    val restResult: PublishSubject<String>  = PublishSubject.create()
    var consumer: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        findViewById(R.id.btn_login).setOnClickListener{
            service.List().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object: Observer<List<String>> {
                        override fun onSubscribe(d: Disposable?) {
                        }

                        override fun onComplete() {

                        }

                        override fun onError(e: Throwable?) {
                            restResult.onError(e)
                        }

                        override fun onNext(t: List<String>?) {
                            t?.forEach { restResult.onNext(it) }
                        }
                    })
        }

        findViewById(R.id.btn_clickme).setOnClickListener {
            service.Post("Hello World:" + Calendar.getInstance().time.toString()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (object :CompletableObserver {
                        override fun onSubscribe(d: Disposable?) {
                        }

                        override fun onComplete() {
//                            restResult.onComplete()
                        }

                        override fun onError(e: Throwable?) {
                            restResult.onError(e)
                        }
                    })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        restResult.subscribe(object : Observer<String> {
            override fun onNext(t: String?) {
                (findViewById(R
                        .id.message) as
                        TextView).text = t
            }

            override fun onComplete() {
//                (findViewById(R
//                        .id.message) as
//                        TextView).text = "Completed"
            }

            override fun onError(e: Throwable?) {
                (findViewById(R
                        .id.message) as
                        TextView).text = e?.message
            }

            override fun onSubscribe(d: Disposable?) {
                consumer = d
            }

        })
    }

    override fun onPause() {
        super.onPause()
        consumer?.dispose()
    }
}
