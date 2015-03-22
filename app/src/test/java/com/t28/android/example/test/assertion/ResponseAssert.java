package com.t28.android.example.test.assertion;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseAssert<T> extends AbstractAssert<ResponseAssert<T>, Response<T>> {

    protected ResponseAssert(Response<T> actual) {
        super(actual, ResponseAssert.class);
    }

    public ResponseAssert<T> hasResult() {
        isNotNull();

        final T actualResult = actual.result;
        assertThat(actualResult)
                .overridingErrorMessage("Expected result does not exist")
                .isNotNull();

        return this;
    }

    public ResponseAssert<T> hasNoResult() {
        isNotNull();

        final T actualResult = actual.result;
        assertThat(actualResult)
                .overridingErrorMessage("Expected result <null> but was <%s>", actualResult)
                .isNull();

        return this;
    }

    public ResponseAssert<T> hasErrorInstanceOf(Class<? extends VolleyError> type) {
        isNotNull();

        final VolleyError actualError = actual.error;
        assertThat(actualError)
                .overridingErrorMessage(
                        "Expected error instance of <%s> but was <%s>",
                        type.getCanonicalName(),
                        actualError.getClass().getCanonicalName()
                )
                .isInstanceOf(type);

        return this;
    }

    public ResponseAssert<T> hasNoError() {
        isNotNull();

        final VolleyError actualError = actual.error;
        assertThat(actualError)
                .overridingErrorMessage("Expected error <null> but was <%s>", actualError)
                .isNull();

        return this;
    }
}
