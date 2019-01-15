package demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author neo
 */
@Controller
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam(value = "name", required = false) String name,
                         @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();

            logger.info("upload file => {} bytes", bytes.length);

            return "successfully uploaded file=" + name;
        } else {
            return "the file is empty";
        }
    }
}
