/*
 * AndroidSensorTest.java
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
import hcm.ssj.core.TheFramework;
import hcm.ssj.signal.Functionals;
import hcm.ssj.test.Logger;

/**
 * Tests the Functionals class.<br>
 * Created by Frank Gaibler on 10.01.2017.
 */
public class FunctionalsTest extends ApplicationTestCase<Application>
{
    //test length in milliseconds
    private final static int TEST_LENGTH = 2 * 60 * 1000;

    /**
     *
     */
    public FunctionalsTest()
    {
        super(Application.class);
    }

    /**
     * @throws Exception
     */
    public void testFunctionals() throws Exception
    {
        //setup
        TheFramework frame = TheFramework.getFramework();
        frame.options.bufferSize.set(10.0f);
        //sensor
        AndroidSensor sensor = new AndroidSensor();
        sensor.options.sensorType.set(SensorType.ACCELEROMETER);

        //channel
        AndroidSensorChannel sensorChannel = new AndroidSensorChannel();
        frame.addSensor(sensor,sensorChannel);
        //transformer
        Functionals transformer = new Functionals();
        frame.addTransformer(transformer, sensorChannel, 1, 0);
        //logger
        Logger log = new Logger();
        frame.addConsumer(log, transformer, 1, 0);
        //start framework
        frame.start();
        //run test
        long end = System.currentTimeMillis() + TEST_LENGTH;
        try
        {
            while (System.currentTimeMillis() < end)
            {
                Thread.sleep(1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        frame.stop();
        frame.release();
    }
}
