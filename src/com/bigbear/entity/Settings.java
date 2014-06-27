package com.bigbear.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luan on 30/07/2013.
 */
public class Settings implements Serializable {
    public Map<String, Account> accounts=new HashMap<String, Account>();
}

