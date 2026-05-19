package io.github.govee.lan.command;

import com.google.gson.annotations.SerializedName;

class GoveeMessage {

    @SerializedName("msg")
    final GoveeCmd msg;

    GoveeMessage(GoveeCmd msg) {
        this.msg = msg;
    }
}
