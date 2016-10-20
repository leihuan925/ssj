/*
 * AccelerationFeaturesTest.java
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
import android.test.suitebuilder.annotation.Suppress;

import java.io.FileReader;

import hcm.ssj.androidSensor.AndroidSensor;
import hcm.ssj.androidSensor.AndroidSensorProvider;
import hcm.ssj.androidSensor.SensorType;
import hcm.ssj.body.AccelerationFeatures;
import hcm.ssj.core.Log;
import hcm.ssj.core.TheFramework;
import hcm.ssj.file.SimpleFileReader;
import hcm.ssj.file.SimpleFileReaderProvider;
import hcm.ssj.file.SimpleFileWriter;
import hcm.ssj.gps.GPSProvider;
import hcm.ssj.gps.GPSSensor;
import hcm.ssj.ml.Classifier;
import hcm.ssj.signal.Avg;
import hcm.ssj.test.Logger;

/**
 * Created by Michael Dietz on 19.10.2016.
 */

public class AccelerationFeaturesTest extends ApplicationTestCase<Application>
{
	// Test length in milliseconds
	private final static int TEST_LENGTH = 2 * 60 * 1000;

	public AccelerationFeaturesTest()
	{
		super(Application.class);
	}

	/**/
	public void test() throws Exception
	{
		// Setup
		TheFramework frame = TheFramework.getFramework();
		frame.options.bufferSize.set(10.0f);

		// Sensor
		AndroidSensor sensor = new AndroidSensor();
		sensor.options.sensorType.set(SensorType.LINEAR_ACCELERATION);
		frame.addSensor(sensor);

		// Provider
		AndroidSensorProvider sensorProvider = new AndroidSensorProvider();
		sensorProvider.options.sampleRate.set(40);
		sensor.addProvider(sensorProvider);

		// Transformer
		AccelerationFeatures features = new AccelerationFeatures();
		frame.addTransformer(features, sensorProvider, 1, 3);

		// SVM
		Classifier classifier = new Classifier();
		classifier.options.trainerPath.set("/sdcard/SSJ/Model/");
		classifier.options.trainerFile.set("my_model.trainer");
		frame.addTransformer(classifier, features, 1, 0);

		// Logger
		Logger log = new Logger();
		frame.addConsumer(log, classifier, 1, 0);

		//Logger log2 = new Logger();
		//frame.addConsumer(log2, features, 1, 0);

		// Start framework
		frame.Start();

		// Run test
		long end = System.currentTimeMillis() + TEST_LENGTH;
		try
		{
			while (System.currentTimeMillis() < end)
			{
				Thread.sleep(1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Stop framework
		frame.Stop();
		frame.reset();
	}
	/**/

	/**
	public void test2() throws Exception
	{
		// Setup
		TheFramework frame = TheFramework.getFramework();
		frame.options.countdown.set(0);
		frame.options.bufferSize.set(10.0f);

		// Sensor
		SimpleFileReader sensor = new SimpleFileReader();
		sensor.options.filePath.set("/sdcard/SSJ/");
		sensor.options.fileName.set("GlassLinearAcceleration.stream");
		sensor.options.loop.set(false);
		frame.addSensor(sensor);

		// Provider
		SimpleFileReaderProvider sensorProvider = new SimpleFileReaderProvider();
		sensorProvider.setSyncInterval(0);
		sensor.addProvider(sensorProvider);

		// Transformer
		AccelerationFeatures features = new AccelerationFeatures();
		frame.addTransformer(features, sensorProvider, 3.2, 3.2);

		// SVM
		Classifier classifier = new Classifier();
		classifier.options.trainerPath.set("/sdcard/SSJ/Model/");
		classifier.options.trainerFile.set("my_model.trainer");
		frame.addTransformer(classifier, features, 3.2, 0);

		// Logger
		Logger log = new Logger();
		frame.addConsumer(log, classifier, 3.2, 0);

		// Start framework
		frame.Start();

		// Run test
		long end = System.currentTimeMillis() + TEST_LENGTH;
		try
		{
			while (System.currentTimeMillis() < end)
			{
				Thread.sleep(1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Stop framework
		frame.Stop();
		frame.reset();
	}
	/**/

	/**
	public void test3() throws Exception
	{
		// Setup
		TheFramework frame = TheFramework.getFramework();
		frame.options.bufferSize.set(10.0f);

		// Sensor
		AndroidSensor sensor = new AndroidSensor();
		sensor.options.sensorType.set(SensorType.LINEAR_ACCELERATION);
		frame.addSensor(sensor);

		// Provider
		AndroidSensorProvider sensorProvider = new AndroidSensorProvider();
		sensorProvider.options.sampleRate.set(40);
		sensor.addProvider(sensorProvider);

		// Logger
		SimpleFileWriter consumer = new SimpleFileWriter();
		consumer.options.fileName.set("TestAcceleration");
		frame.addConsumer(consumer, sensorProvider, 1, 0);

		// Start framework
		frame.Start();

		// Run test
		long end = System.currentTimeMillis() + TEST_LENGTH;
		try
		{
			while (System.currentTimeMillis() < end)
			{
				Thread.sleep(1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Stop framework
		frame.Stop();
		frame.reset();
	}
	/**/
}