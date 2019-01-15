package core.framework.monitor.web;

import core.framework.monitor.MonitorAccessControl;
import core.framework.web.request.RequestContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * @author neo
 */
@RestController
public class MemoryUsageController {
    @Inject
    RequestContext requestContext;

    @RequestMapping(value = "/monitor/memory", produces = "text/plain", method = RequestMethod.GET)
    @ResponseBody
    public String memoryUsage() {
        MonitorAccessControl.assertFromInternalNetwork(requestContext.clientIP());

        StringBuilder builder = new StringBuilder(500);
        MemoryUsage heapUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        builder.append("Heap Usage:\n")
            .append(heapUsage)
            .append("\n\nNonHeap Usage:\n");

        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        builder.append(nonHeapMemoryUsage)
            .append("\n\nMemory Pools:\n");

        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean pool : pools) {
            builder.append(pool.getName()).append(" (").append(pool.getType()).append(")\n")
                .append(pool.getUsage())
                .append('\n');
        }

        builder.append("\nLast Collection Usage:\n");
        for (MemoryPoolMXBean pool : pools) {
            builder.append(pool.getName()).append(" (").append(pool.getType()).append(")\n")
                .append(pool.getCollectionUsage())
                .append('\n');
        }

        return builder.toString();
    }
}
