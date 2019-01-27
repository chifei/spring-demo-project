package app.demo.product.web;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.opencsv.CSVWriter;
import app.demo.product.domain.Product;
import app.demo.product.service.ProductService;
import app.demo.product.web.product.ProductQuery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;


@Controller
public class ProductController {

    @Inject
    ProductService productService;

    @RequestMapping("/admin/product/template/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=product_template.csv");
        byte[] bytes = ByteStreams.toByteArray(this.getClass().getResourceAsStream("/download/product_template.csv"));
        FileCopyUtils.copy(bytes, response.getOutputStream());
    }

    @RequestMapping("/admin/product/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) String name) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=product.csv");
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(response.getOutputStream()));
        ProductQuery productQuery = new ProductQuery();
        productQuery.name = name;
        productQuery.page = 1;
        productQuery.limit = 1000;
        List<Product> productList = Lists.newArrayList();
        Page<Product> productPage = productService.find(productQuery);
        productList.addAll(productPage.getContent());
        while (productPage.getTotalPages() > productQuery.page) {
            productQuery.page = productQuery.page + 1;
            productPage = productService.find(productQuery);
            productList.addAll(productPage.getContent());
        }
        csvWriter.writeNext(new String[]{"Product Name", "Product Description"});
        for (Product product : productList) {
            csvWriter.writeNext(new String[]{product.name, product.description});
        }
        csvWriter.close();
    }

}
