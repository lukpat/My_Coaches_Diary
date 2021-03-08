package cz.lpatak.mycoachesdiary

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyCoachesDiaryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyCoachesDiaryApp)
            modules(appModule)
        }
    }
}