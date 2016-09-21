package com.bikefit.wedgecalculator.example;

import android.content.res.Resources;

import com.bikefit.wedgecalculator.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ExampleControllerUnitTest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @InjectMocks
    ExampleController mExampleController;

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------

    @Mock
    Resources mMockResources;

    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testGetAppName() throws Exception {
        // GIVEN the resources returns an expected string for app name
        String testAppName = "test_app";
        when(mMockResources.getString(R.string.app_name)).thenReturn(testAppName);

        // WHEN the app name is fetched from the controller
        String resourceAppName = mMockResources.getString(R.string.app_name);

        // THEN it's the expected string
        assertEquals(testAppName, resourceAppName);

    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion
}