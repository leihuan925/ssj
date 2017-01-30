/*
 * FFTTest.java
 * Copyright (c) 2017
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

import hcm.ssj.core.TheFramework;
import hcm.ssj.file.SimpleFileReader;
import hcm.ssj.file.SimpleFileReaderChannel;
import hcm.ssj.file.SimpleFileWriter;
import hcm.ssj.signal.FFTfeat;

/**
 * Created by Michael Dietz on 19.10.2016.
 */

public class FFTTest extends ApplicationTestCase<Application>
{
	// Test length in milliseconds
	private final static int TEST_LENGTH = 26 * 1000;

	public FFTTest()
	{
		super(Application.class);
	}

	/**/
	public void test() throws Exception
	{
		// Setup
		TheFramework frame = TheFramework.getFramework();
		frame.options.bufferSize.set(10.0f);
		frame.options.countdown.set(0);

		// Sensor
		SimpleFileReader file = new SimpleFileReader();
		file.options.fileName.set("audio.stream");
		SimpleFileReaderChannel channel = new SimpleFileReaderChannel();
		channel.setWatchInterval(0);
		channel.setSyncInterval(0);
		frame.addSensor(file, channel);

		// Transformer
		FFTfeat fft = new FFTfeat();
		frame.addTransformer(fft, channel, 512.0 / channel.getSampleRate(), 0);

		SimpleFileWriter sfw = new SimpleFileWriter();
		frame.addConsumer(sfw, fft, 1, 0);

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

}
