package com.awssdjava.awsdemo;

import lombok.Data;

@Data
public class SpringCloudMetricsData {
   private long mem;
   private long processors;
   private long heap;
   private long gc_ps_scavenge_count;
   private long uptime;
}
