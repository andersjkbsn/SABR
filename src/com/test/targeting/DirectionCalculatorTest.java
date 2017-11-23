package com.test.targeting;

import com.ballthrower.targeting.DirectionCalculator;
import com.ballthrower.targeting.ITargetBoxInfo;
import com.ballthrower.targeting.TargetBoxInfo;
import com.ballthrower.tools.CSVReader;
import com.ballthrower.tools.TestTargetBoxInfo;
import com.ballthrower.exceptions.AssertException;
import com.test.NXTAssert;
import com.test.NXTTest;

import java.util.ArrayList;


public class DirectionCalculatorTest
{
    private DirectionCalculator dc;
    private ITargetBoxInfo tbi;

    private void setUp()
    {
        dc = new DirectionCalculator();
        tbi = NXTTest.getTestTargetBox();
    }

    private void sampleCountZeroTest() throws AssertException
    {
        TargetBoxInfo zeroSample = new TargetBoxInfo((byte)0);

        NXTAssert test = new NXTAssert();

        test.assertThat(dc.calculateMeanPixelDistance(zeroSample), "DirectionCalculator:sampleCountZeroTest")
                .isEqualTo(Float.NEGATIVE_INFINITY);
    }

    private void meanPixelDistanceTest() throws AssertException
    {
        /* Calculated beforehand */
        float actualMeanPixelDistance = -300.5F;

        tbi = NXTTest.getTestTargetBox();
        tbi.setFrameWidth((short)1600);

        NXTAssert test = new NXTAssert();

        test.assertThat(dc.calculateMeanPixelDistance(tbi), "DirectionCalculator:meanPixelDistanceTest")
                .isEqualTo(actualMeanPixelDistance);
    }

    public void runAllTests() throws AssertException
    {
        setUp();
        sampleCountZeroTest();
        meanPixelDistanceTest();
    }
}

