package com.pernal.persistence;

public enum DataColumns {
    PRIMARY_KEY(1), NAME(2), DESCRIPTION(3), UPDATED_TIMESTAMP(4);

    private int index;

    DataColumns(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
