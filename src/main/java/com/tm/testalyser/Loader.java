package com.tm.testalyser;

import java.util.List;

public interface Loader<T> {

    List<T> load();
}
