package io.github.govee.lan.command;

import com.google.gson.annotations.SerializedName;

class GoveeCmd {

    @SerializedName("cmd")
    final String cmd;

    @SerializedName("data")
    final Object data;

    GoveeCmd(String cmd, Object data) {
        this.cmd = cmd;
        this.data = data;
    }
}
