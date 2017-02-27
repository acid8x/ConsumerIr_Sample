package com.example.acid8xtreme.consumerir;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.hardware.ConsumerIrManager;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsumerIr extends Activity implements View.OnClickListener {

    private static final String TAG = "ConsumerIrTest";

    ConsumerIrManager mCIR;
    Button power,volneg,voladd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCIR = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);
        setContentView(R.layout.consumer_ir);
        power = (Button) findViewById(R.id.power);
        power.setOnClickListener(this);
        voladd = (Button) findViewById(R.id.voladd);
        voladd.setOnClickListener(this);
        volneg = (Button) findViewById(R.id.volneg);
        volneg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!mCIR.hasIrEmitter()) {
            Log.e(TAG, "No IR Emitter found\n");
            return;
        }
        switch (v.getId()) {
            case R.id.power:
                sendIR(getString(R.string.power));
                break;
            case R.id.voladd:
                sendIR(getString(R.string.voladd));
                break;
            case R.id.volneg:
                sendIR(getString(R.string.volneg));
                break;
        }
    }

    private void sendIR(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2
        frequency = (int) (1000000 / (frequency * 0.241246));
        double pulses = 1000000/frequency;
        int[] pattern = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            pattern[i] = (int)(Integer.parseInt(list.get(i), 16) * pulses);
        }
        mCIR.transmit(frequency, pattern);
    }
}
