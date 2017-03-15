/*
 * testEvent.java
 * Copyright (c) 2016
 * Authors: Ionut Damian, Michael Dietz, Frank Gaibler, Daniel Langerenken, Simon Flutura
 * *****************************************************
 * This file is part of the Social Signal Interpretation for Java (SSJ) framework
 * developed at the Lab for Human Centered Multimedia of the University of Augsburg.
 *
 * SSJ has been inspired by the SSI (http://openssi.net) framework. SSJ is not a
 * one-to-one port of SSI to Java, it is an approximation. Nor does SSJ pretend
 * to offer SSI's comprehensive functionality and performance (this is java after all).
 * Nevertheless, SSJ borrows a lot of programming patterns from SSI.
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package hcm.ssj;

import android.app.Application;
import android.test.ApplicationTestCase;

import hcm.ssj.androidSensor.AndroidSensor;
import hcm.ssj.androidSensor.AndroidSensorChannel;
import hcm.ssj.androidSensor.SensorType;
import hcm.ssj.audio.AudioChannel;
import hcm.ssj.audio.Microphone;
import hcm.ssj.core.EventChannel;
import hcm.ssj.core.Pipeline;
import hcm.ssj.core.Provider;
import hcm.ssj.event.FloatSegmentEventSender;
import hcm.ssj.event.FloatsEventSender;
import hcm.ssj.event.ThresholdEventSender;
import hcm.ssj.file.FileEventWriter;
import hcm.ssj.file.FileWriter;
import hcm.ssj.praat.Intensity;
import hcm.ssj.test.EventLogger;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class testEvent extends ApplicationTestCase<Application> {
    public testEvent() {
        super(Application.class);
    }

    public void test() throws Exception
    {
        Pipeline frame = Pipeline.getInstance();
        frame.options.bufferSize.set(10.0f);

        AndroidSensor sensor = new AndroidSensor();
        sensor.options.sensorType.set(SensorType.ACCELEROMETER);
        AndroidSensorChannel acc = new AndroidSensorChannel();
        acc.options.sampleRate.set(40);
        frame.addSensor(sensor, acc);

        FloatsEventSender evs = new FloatsEventSender();
        evs.options.mean.set(true);
        frame.addConsumer(evs, acc, 1.0, 0);
        EventChannel channel = evs.getEventChannelOut();

        EventLogger log = new EventLogger();
        frame.registerEventListener(log, channel);

        FileEventWriter write = new FileEventWriter();
        write.options.format.set(FileEventWriter.Format.EVENT);
        frame.registerEventListener(write, channel);

        FileWriter fw = new FileWriter();
        frame.addConsumer(fw, acc, 1.0, 0);

        try {
            frame.start();

            long start = System.currentTimeMillis();
            while(true)
            {
                if(System.currentTimeMillis() > start + 1 * 10 * 1000)
                    break;

                Thread.sleep(1);
            }

            frame.stop();
            frame.release();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void test2() throws Exception
    {
        Pipeline frame = Pipeline.getInstance();
        frame.options.bufferSize.set(10.0f);

        Microphone mic = new Microphone();
        AudioChannel audio = new AudioChannel();
        audio.options.sampleRate.set(16000);
        audio.options.scale.set(true);
        frame.addSensor(mic,audio);


        Intensity energy = new Intensity();
        frame.addTransformer(energy, audio, 1.0, 0);

        ThresholdEventSender vad = new ThresholdEventSender();
        vad.options.thresin.set(new float[]{50.0f}); //SPL
        vad.options.mindur.set(1.0);
        vad.options.maxdur.set(9.0);
        vad.options.hangin.set(3);
        vad.options.hangout.set(5);
        Provider[] vad_in = {energy};
        frame.addConsumer(vad, vad_in, 1.0, 0);
        EventChannel vad_channel = vad.getEventChannelOut();

        FloatSegmentEventSender evs = new FloatSegmentEventSender();
        evs.options.mean.set(true);
        frame.addConsumer(evs, energy, vad_channel);
        EventChannel channel = evs.getEventChannelOut();

        EventLogger log = new EventLogger();
        frame.registerEventListener(log, channel);

        try {
            frame.start();

            long start = System.currentTimeMillis();
            while(true)
            {
                if(System.currentTimeMillis() > start + 10000 * 10 * 1000)
                    break;

                Thread.sleep(1);
            }

            frame.stop();
            frame.release();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}