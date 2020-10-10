import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

/**
 * @Description: 获取服务器IP
 * @Author: liyue
 * @Date: 2020-06-11 10:51
 * @Version: 1.0
 **/
public class ServerIpUtils {
    private static Logger logger = LoggerFactory.getLogger(ServerIpUtils.class);
    public static ArrayList<String> getLocalIpAddr()
    {
        ArrayList<String> ipList = new ArrayList<String>();
        InetAddress[] addrList;
        try
        {
            Enumeration interfaces= NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements())
            {
                NetworkInterface ni=(NetworkInterface)interfaces.nextElement();
                Enumeration ipAddrEnum = ni.getInetAddresses();
                while(ipAddrEnum.hasMoreElements())
                {
                    InetAddress addr = (InetAddress)ipAddrEnum.nextElement();
                    if (addr.isLoopbackAddress() == true)
                    {
                        continue;
                    }

                    String ip = addr.getHostAddress();
                    if (ip.indexOf(":") != -1)
                    {
                        //skip the IPv6 addr
                        continue;
                    }

                    logger.debug("Interface: " + ni.getName()
                            + ", IP: " + ip);
                    ipList.add(ip);
                }
            }

            Collections.sort(ipList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("Failed to get local ip list. " + e.getMessage());
            throw new RuntimeException("Failed to get local ip list");
        }

        return ipList;
    }

    public static void getLocalIpAddr(Set<String> set)
    {
        ArrayList<String> addrList = getLocalIpAddr();
        set.clear();
        for (String ip : addrList)
        {
            if(!ip.equals("127.0.0.1") && !ip.equals("192.168.122.1")){
                set.add(ip);
            }
        }
    }

}
