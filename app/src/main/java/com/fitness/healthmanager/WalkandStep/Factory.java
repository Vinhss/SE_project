
package com.fitness.healthmanager.WalkandStep;

import android.content.pm.PackageManager;

import com.fitness.healthmanager.WalkandStep.services.AbstractStepDetectorService;
import com.fitness.healthmanager.WalkandStep.services.AccelerometerStepDetectorService;
import com.fitness.healthmanager.WalkandStep.services.HardwareStepDetectorService;
import com.fitness.healthmanager.WalkandStep.utils.AndroidVersionHelper;




public class Factory {



    public static Class<? extends AbstractStepDetectorService> getStepDetectorServiceClass(PackageManager pm){
        if(pm != null && AndroidVersionHelper.supportsStepDetector(pm)) {
            return HardwareStepDetectorService.class;
        }else{
            return AccelerometerStepDetectorService.class;
        }
    }
}
