package pt.hventura.mycoktails.cocktails.randomcocktail

import android.graphics.drawable.AnimationDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.authentication.AuthenticationActivity
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.databinding.FragmentRandomCocktailBinding
import pt.hventura.mycoktails.utils.LoginControl
import pt.hventura.mycoktails.utils.finish
import pt.hventura.mycoktails.utils.startActivity
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.sqrt

class RandomCocktailFragment : BaseFragment(), SensorEventListener {

    private lateinit var binding: FragmentRandomCocktailBinding
    private lateinit var frameAnimation: AnimationDrawable
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private var accelerationCurrentValue = 0F
    private var accelerationPreviousValue = 0F
    private var changeInAcceleration = 0F
    override val viewModel: RandomCocktailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_random_cocktail, container, false)

        if (!LoginControl.isLoggedIn()) {
            startActivity<AuthenticationActivity>()
            finish()
        }

        mSensorManager = ContextCompat.getSystemService(requireContext(), SensorManager::class.java) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        viewModel.shakeMax.observe(requireActivity()) {
            if (it >= 15) {
                mSensorManager.unregisterListener(this)
                val vibe: Vibrator = ContextCompat.getSystemService(requireContext(), Vibrator::class.java) as Vibrator
                val effect: VibrationEffect = VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE)
                vibe.vibrate(effect)
                viewModel.loadRandomCocktail()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.randomLoadingImage.setBackgroundResource(R.drawable.cocktail_animation_list)
        frameAnimation = binding.randomLoadingImage.background as AnimationDrawable
        frameAnimation.start()
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val x: Float = sensorEvent.values[0]
        val y: Float = sensorEvent.values[1]
        val z: Float = sensorEvent.values[2]
        accelerationCurrentValue = sqrt((x * x + y * y + z * z))
        changeInAcceleration = abs(accelerationCurrentValue - accelerationPreviousValue)
        accelerationPreviousValue = accelerationCurrentValue
        viewModel.updateMaxShake(changeInAcceleration)
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
        Timber.i("What is i? -> $i")
    }

}