package com.test.communication;

import com.ballthrower.abortion.AbortCode;
import com.ballthrower.abortion.IAbortable;
import com.ballthrower.communication.Connection;
import com.ballthrower.communication.ConnectionFactory;
import com.ballthrower.exceptions.AssertException;
import com.test.NXTAssert;
import com.test.Test;
import lejos.nxt.comm.NXTConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BluetoothConnectionTest extends Test implements IAbortable
{
    private NXTConnection _socket;
    private DataInputStream _inputStream;
    private DataOutputStream _outputStream;

    private Connection _bc;

    public void setUp()
    {
        _bc = new ConnectionFactory().createInstance(ConnectionFactory.ConnectionType.Bluetooth, this);
    }

    public BluetoothConnectionTest()
    {
        setUp();
    }

    public void awaitConnectionTest() throws AssertException
    {
        try {
            _bc.awaitConnection();
            NXTAssert test = new NXTAssert();

            test.assertThat(_socket.available(0), "BluetoothConnection:awaitConnection")
                    .isTrue();
            test.assertThat(_inputStream.available(), "BluetoothConnection:awaitConnection")
                    .isTrue();
        }
        catch (IOException e)
        {
            /* Not relevant for testing... */
        }
    }

    public void runAllTests() throws AssertException
    {
        awaitConnectionTest();
    }

    @Override
    public void abort(AbortCode code) {

    }

    @Override
    public void abort(AbortCode code, String message) {

    }

    @Override
    public void warn(String message) {

    }
}
