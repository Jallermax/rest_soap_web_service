package com.aplana.apiPractice;

import com.aplana.apiPractice.models.Profile;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskValidation {
    private static final Logger LOG = Log.getLogger(TaskValidation.class);
    private static final int threshold = 2;
    private ServiceType serviceType;

    public TaskValidation(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String checkValid(Collection<Profile> profiles) {
        //TODO check profiles and return key
        AtomicInteger counter = new AtomicInteger();
        profiles.forEach(profile -> {
            if (profile.valid()) {
                counter.incrementAndGet();
                LOG.debug("Profile " + profile.getId() + " (" + profile.getFullName() + ") is valid");
            } else {
                LOG.debug("Profile " + profile.getId() + " (" + profile.getFullName() + ") is invalid");
            }
        });
        return counter.get() > 2 ? serviceType.getAnswerKey() : "You need to add " + (threshold - counter.get()) + " more valid profiles";
    }

    public enum ServiceType {
        REST("RestKey_23576653"), SOAP("SoapKey_88135789");
        private String answerKey;

        ServiceType(String answerKey) {
            this.answerKey = answerKey;
        }

        public String getAnswerKey() {
            return answerKey;
        }
    }
}
