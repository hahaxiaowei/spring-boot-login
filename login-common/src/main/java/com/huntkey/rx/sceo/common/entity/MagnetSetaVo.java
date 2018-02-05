package com.huntkey.rx.sceo.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lulx on 2018/1/2 0002 上午 9:26
 */
public class MagnetSetaVo {
    List<MagnetVo> magnetSetaStart;
    List<MagnetVo> magnetSetaDashboard;

    public MagnetSetaVo() {
        this.magnetSetaStart = new ArrayList<MagnetVo>();
        this.magnetSetaDashboard = new ArrayList<MagnetVo>();
    }

    public List<MagnetVo> getMagnetSetaStart() {
        return magnetSetaStart;
    }

    public void setMagnetSetaStart(List<MagnetVo> magnetSetaStart) {
        this.magnetSetaStart = magnetSetaStart;
    }

    public boolean addMagnetSetaStart(MagnetVo magnetVo) {
        return this.magnetSetaStart.add(magnetVo);
    }

    public void addMagnetSetaDashboard(MagnetVo magnetVo) {
        this.magnetSetaDashboard.add(magnetVo);
    }

    public List<MagnetVo> getMagnetSetaDashboard() {
        return magnetSetaDashboard;
    }

    public void setMagnetSetaDashboard(List<MagnetVo> magnetSetaDashboard) {
        this.magnetSetaDashboard = magnetSetaDashboard;
    }
}
