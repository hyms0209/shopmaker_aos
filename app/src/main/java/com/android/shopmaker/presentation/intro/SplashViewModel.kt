package com.android.shopmaker.presentation.intro

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.shopmaker.data.service.network.vo.AppUpdateData
import com.android.shopmaker.data.service.network.vo.EmergencyNoticeData

interface SplashViewModelType {
    var input: SplashViewModelInput
    var output: SplashViewModelOutput
}

interface SplashViewModelInput {
    fun checkEmergency(result:Boolean)
    fun checkAppUpdate(result:Boolean)
    fun checkPermission(result:Boolean)
}

interface SplashViewModelOutput {
    val checkEmergency:LiveData<EmergencyNoticeData>
    val checkAppUpdate:LiveData<AppUpdateData>
    val checkPermission:LiveData<Boolean>
    val goMain:LiveData<Boolean>
}

enum class JobStep(  // 메인이동
    var requestCode: Int
) {

    Emergency(2000),    // 긴급공지
    Update(2001),       // 업데이트 체크
    Permission(2003),   // 권한동의 ( 백그라운드 위치 권한 필수 적용)
    Main(2006);         // 메인화면 이동

    var bundle: Bundle? = null
}

class SplashViewModel:ViewModel(),
    SplashViewModelType,
    SplashViewModelInput,
    SplashViewModelOutput
{
    override var input: SplashViewModelInput = this
    override var output: SplashViewModelOutput = this

    private var _checkEmergency  =  MutableLiveData<EmergencyNoticeData>()
    override val checkEmergency:LiveData<EmergencyNoticeData>
        get() = _checkEmergency

    private var _checkAppUpdate  =  MutableLiveData<AppUpdateData>()
    override val checkAppUpdate:LiveData<AppUpdateData>
        get() = _checkAppUpdate

    private var _checkPermission  =  MutableLiveData<Boolean>()
    override val checkPermission:LiveData<Boolean>
        get() = _checkPermission

    private var _goMain  =  MutableLiveData<Boolean>()
    override val goMain:LiveData<Boolean>
        get() = _goMain

    //val repository = IninProcessRepository()

    var task:ArrayList<JobStep> = arrayListOf()

    init {
        task.add(JobStep.Emergency)
        task.add(JobStep.Update)
        task.add(JobStep.Permission)
        task.add(JobStep.Main)
    }

    /***
     * 타스크 단위 잡스탭 시작
     */
    fun start() {
        onNext()
    }

    /***
     *  다음 잡스탭 진행
     */
    fun onNext() {
        if (task.size > 0) {
            val step = task.removeAt(0)
            when (step) {
                JobStep.Emergency -> goEmergency()
                JobStep.Update -> goUpdate()
                JobStep.Permission -> goPermission()
                JobStep.Main -> goMain()
            }
        }
    }

    fun goEmergency() {
//        repository.requestEmergencyNotice()
//            .subscribeOn(Schedulers.io())
//            .subscribe( {
//                _checkEmergency.postValue(it)
////                if ( it.isShow == "Y") {
////                    _checkEmergency.postValue(it)
        _checkEmergency.postValue(EmergencyNoticeData(

        ))
////                } else {
////                    onNext()
////                }
//                Log.d("Emergency Subscribe", "긴급공지 취득 성공")
//            }, {
//                Log.d("Emergency Throwable", "긴급공지 취득 오류")
//            },{
//                Log.d("Emergency Complete","긴급공지 취득 완료")
//                onNext()
//            })
//        onNext()
    }

    fun goUpdate() {
//        repository.requestAppUpdate("AOS", "0.8")
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                var currentVersion = BuildConfig.VERSION_NAME
//                var compAppVersion =  currentVersion.removeSurrounding(".")
//                var compLatestVersion = it.version.removeSurrounding(".")
//                // 업데이트
//                if ( compLatestVersion > compAppVersion) {
//                    if ( it.status == "FORCE" ) {
//                        it.title = "앱 업데이트"
//                        it.message = "최신버젼이 ${it.version}입니다.\n앱을 사용하기 위해서는 반드시 업데이트가 필요합니다."
//                    } else {
//                        it.title = "앱 업데이트"
//                        it.message = "최신버젼이 ${it.version}입니다.\n앱을 최신버젼으로 업데이트 하시겠습니까?"
//                    }
//                    _checkAppUpdate.postValue(it)
                      _checkAppUpdate.postValue(AppUpdateData())
//                } else {
//                    onNext()
//                }
//            }, {
//
//            }, {
//                onNext()
//            })
//        onNext()
    }

    fun goPermission() {
        _checkPermission.postValue(true)
    }

    fun goMain() {
       _goMain.postValue(true)
    }

    override fun checkEmergency(result: Boolean) {
        if ( result ) { onNext() }
    }

    override fun checkAppUpdate(result: Boolean) {
        if ( result ) { onNext() }
    }

    override fun checkPermission(result: Boolean) {
        if ( result ) { onNext() }
    }
}