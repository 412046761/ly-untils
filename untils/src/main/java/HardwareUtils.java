import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.Serializable;

/**
 * @Description:  监控硬件使用率
 * @Author: liyue
 * @Date: 2020-06-08 15:37
 * @Version: 1.0
 **/
@Service
public class HardwareUtils implements Serializable {
    private static final long serialVersionUID = 947063775242443474L;
    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * 获取CPU 使用率
     */
    private String getCpuUsage(CentralProcessor processor) {
        // CPU信息 第一次截取信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        Long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        Long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        Long system = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        Long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        Long iowait  = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        Long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        Long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        Long totalCpu = user + nice + idle + system + iowait + irq + softirq;

        double v = ((totalCpu.doubleValue() - idle.doubleValue()) / totalCpu.doubleValue()) *100;
        System.out.println("CPU使用："+(totalCpu.doubleValue() - idle.doubleValue()));
        System.out.println("totalCpu："+totalCpu.doubleValue());

        if(v > new Double(100)){
            return "100";
        }else{
            String usage = String.format("%.2f", (v));
            return usage;
        }
    }

    /**
     * 获取内存使用率
     */
    private String getMemUsage(GlobalMemory memory) {
        Long total = memory.getTotal();
        Long used = memory.getTotal() - memory.getAvailable();
        double v = ((used.doubleValue() / total.doubleValue()) * 100);
        System.out.println("磁盘使用率used："+v+"=used:"+used+"/total:"+total);
        if(v > new Double(100)){
            return "100";
        }else{
        String usage = String.format("%.2f",v );
        return usage;
        }
    }

    /**
     * 获取磁盘使用率
     */
    private String getDiskUsage(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        Long total = new Long(0);
        Long allfree = new Long(0);
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long totalItem = fs.getTotalSpace() ;
            allfree += free;
            total += totalItem;
        }
        Long used = total - allfree;
        System.out.println("磁盘使用率used："+used.doubleValue()+"=total:"+total+"-allfree:"+allfree);
        System.out.println("磁盘total："+total);
        double v = ((used.doubleValue() / total.doubleValue()) * 100);
        if(v > new Double(100)){
            return "100";
        }else {
            String usage = String.format("%.2f", v);
            return usage;
        }
    }
}
