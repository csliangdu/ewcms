package com.ewcms.publication.task.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ewcms.publication.task.TaskException;
import com.ewcms.publication.task.Taskable;
import com.ewcms.publication.task.impl.process.TaskProcessable;

/**
 * 空的任务
 * 
 * @author wangwei
 */
public class NoneTask extends TaskBase{

    public NoneTask(){
        super(newTaskId());
    }
    
    @Override
    public String getDescription() {
        return "None task";
    }

    @Override
    public Integer getSiteId() {
        return -1;
    }

    @Override
    public String getUsername() {
        return DEFAULT_USERNAME;
    }

    @Override
    public List<Taskable> getDependences() {
        return Collections.unmodifiableList(new ArrayList<Taskable>(0));
    }

    @Override
    public void close() throws TaskException {
        // do not instance
        
    }

    @Override
    protected List<TaskProcessable> getTaskProcesses() throws TaskException {
        return Collections.unmodifiableList(new ArrayList<TaskProcessable>(0));
    }
}