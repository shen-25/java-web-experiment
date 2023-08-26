package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.AdviseCommand;
import com.jingdong.manager.model.entity.Advise;
import com.jingdong.manager.service.AdviseService;

import java.util.List;

public class AdviseServiceImpl implements AdviseService {

    private AdviseCommand adviseCommand = new AdviseCommand();
    @Override
    public List<Advise> selectAll() {
        return adviseCommand.selectAll();
    }
}
