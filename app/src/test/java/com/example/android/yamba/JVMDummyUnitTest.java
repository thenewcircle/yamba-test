package com.example.android.yamba;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assert_;

public class JVMDummyUnitTest {

    @Test
    public void dummyUnitTest() {
        // do nothing but compile, run, and pass
    }

    @Test
    public void emptyListHasNoItems() {
        //Set up collections to evaluate
        List<String> empty = Collections.emptyList();

        assertThat(empty)
                .named("empty list")
                .isEmpty();
    }

    @Test
    public void singleListHasOneItem() {
        //Set up collection to evaluate
        List<String> single = Collections.singletonList("Android");

        assertThat(single)
                .named("single item list")
                .hasSize(1);
    }

    @Test
    public void stringListHasAllItems() {
        //Set up collection to evaluate
        List<String> list = Arrays.asList("Android", "Google", "NewCircle");

        assert_().withMessage("missing items")
                .that(list)
                .named("item list")
                .containsExactly("Android", "Google", "NewCircle");
    }
}
