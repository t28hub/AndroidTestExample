package com.t28.android.example.test.assertion;

import android.net.Uri;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class UriAssert extends AbstractAssert<UriAssert, Uri> {
    protected UriAssert(Uri uri) {
        super(uri, UriAssert.class);
    }

    public UriAssert hasScheme(String scheme) {
        isNotNull();

        final String actualScheme = actual.getScheme();
        assertThat(actualScheme)
                .overridingErrorMessage("Expected scheme <%s> but was <%s>.", scheme, actualScheme)
                .isEqualTo(scheme);

        return this;
    }

    public UriAssert hasAuthority(String authority) {
        isNotNull();

        final String actualAuthority = actual.getAuthority();
        assertThat(actualAuthority)
                .overridingErrorMessage("Expected authority <%s> but was <%s>.", authority, actualAuthority)
                .isEqualTo(authority);

        return this;
    }

    public UriAssert hasHost(String host) {
        isNotNull();

        final String actualHost = actual.getHost();
        assertThat(actualHost)
                .overridingErrorMessage("Expected host <%s> but was <%s>.", host, actualHost)
                .isEqualTo(host);

        return this;
    }

    public UriAssert hasPath(String path) {
        isNotNull();

        final String actualPath = actual.getPath();
        assertThat(actualPath)
                .overridingErrorMessage("Expected path <%s> but was <%s>.", path, actualPath)
                .isEqualTo(path);

        return this;
    }

    public UriAssert hasQueryParameter(String key, String parameter) {
        isNotNull();

        final String actualParameter = actual.getQueryParameter(key);
        assertThat(actualParameter)
                .overridingErrorMessage("Expected parameter <%s> but was <%s>.", parameter, actualParameter)
                .isEqualTo(parameter);

        return this;
    }
}
