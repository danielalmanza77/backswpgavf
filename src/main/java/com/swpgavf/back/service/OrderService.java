package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.dto.ProductOrderItemDTO;
import com.swpgavf.back.entity.Order;
import com.swpgavf.back.entity.OrderItem;
import com.swpgavf.back.entity.Payment;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.exception.ResourceNotFoundException;
import com.swpgavf.back.repository.IOrderRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public OrderService(IOrderRepository orderRepository, ObjectMapper objectMapper, ProductService productService) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        Order order = mapToEntity(orderRequestDTO);
        orderRepository.save(order);
        return mapToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = findOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);  // Persist the updated order
    }

    private OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setOrderDate(order.getOrderDate());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setAmount(order.getAmount());
        responseDTO.setCurrency(order.getCurrency());

        // Map order items (product + quantity) to ProductOrderItemDTO
        List<ProductOrderItemDTO> productOrderItems = order.getOrderItems().stream()
                .map(orderItem -> {
                    Product product = orderItem.getProduct();
                    ProductOrderItemDTO itemDTO = new ProductOrderItemDTO();
                    itemDTO.setId(product.getId());
                    itemDTO.setSku(product.getSku());
                    itemDTO.setName(product.getName());
                    itemDTO.setDescription(product.getDescription());
                    itemDTO.setCategory(product.getCategory());
                    itemDTO.setStock(product.getStock());
                    itemDTO.setPrice(product.getPrice());
                    itemDTO.setBrand(product.getBrand());
                    itemDTO.setImageUrls(product.getImageUrls());
                    itemDTO.setAvailable(product.getAvailable());
                    itemDTO.setQuantity(orderItem.getQuantity()); // Set quantity
                    return itemDTO;
                })
                .collect(Collectors.toList());

        responseDTO.setProducts(productOrderItems);
        return responseDTO;
    }

    private Order mapToEntity(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(orderRequestDTO.getStatus());

        // Fetch products and create the corresponding OrderItem objects
        List<OrderItem> orderItems = orderRequestDTO.getProducts().stream()
                .map(orderItemDTO -> {
                    // Assuming the OrderItemDTO has getProductId() and getQuantity()
                    Product product = productService.getProductEntityById(orderItemDTO.getProductId()); // Adjust this line if productId is named differently in OrderItemDTO

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product); // Set the product
                    orderItem.setQuantity(orderItemDTO.getQuantity()); // Set the quantity from OrderItemDTO
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems); // Make sure your Order entity has a field for the list of OrderItems
        order.calculateTotalAmount(); // This should calculate the total amount based on the OrderItems
        order.setCurrency("pen"); // Or dynamically set the currency

        return order;
    }
//    public void generateExcel(HttpServletResponse response) throws IOException {
//        List<Order> payments = orderRepository.findAll();
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Orders");
//        HSSFRow row = sheet.createRow(0);
//
//        row.createCell(0).setCellValue("Order ID");
//        row.createCell(1).setCellValue("Order Date");
//        row.createCell(2).setCellValue("Status");
//        row.createCell(3).setCellValue("Amount");
//        row.createCell(4).setCellValue("Currency");
//
//
//        int dataRowIndex = 1;
//
//        for (Order order : payments) {
//            HSSFRow row1 = sheet.createRow(dataRowIndex);
//            row1.createCell(0).setCellValue(order.getId());
//            row1.createCell(1).setCellValue(order.getOrderDate());
//            row1.createCell(2).setCellValue(order.getStatus());
//            row1.createCell(3).setCellValue(order.getAmount());
//            row1.createCell(4).setCellValue(order.getCurrency());
//            dataRowIndex++;
//        }
//
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//        outputStream.close();
//
//    }

    public void generateExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        List<Order> payments = orderRepository.findByOrderDateBetweenAndStatus(startDate, endDate, "PAID");

        XSSFWorkbook workbook = new  XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Orders");


        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Order ID");
        row.createCell(1).setCellValue("Order Date");
        row.createCell(2).setCellValue("Amount");
        row.createCell(3).setCellValue("Currency");
        row.createCell(4).setCellValue("Status");

        for (int i = 0; i < 5; i++) {
            row.getCell(i).setCellStyle(style);
        }
        int dataRowIndex = 1;

        for (Order order : payments) {
            XSSFRow row1 = sheet.createRow(dataRowIndex);
            row1.createCell(0).setCellValue(order.getId());
            row1.createCell(1).setCellValue(order.getOrderDate().toString());
            row1.createCell(2).setCellValue(order.getAmount());
            row1.createCell(3).setCellValue(order.getCurrency());
            row1.createCell(4).setCellValue(order.getStatus());


            for (int i = 0; i < 5; i++) {
                row1.getCell(i).setCellStyle(style);
            }

            dataRowIndex++;
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


//    public void generatePDF(HttpServletResponse response) throws IOException {
//
//        List<Order> payments = orderRepository.findAll();
//        Document document = new Document(PageSize.A4);
//        document.addTitle("ORDERS");
//        PdfWriter.getInstance(document, response.getOutputStream());
//
//        document.open();
//        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
//        fontTitle.setSize(18);
//
//        Paragraph paragraph = new Paragraph("REPORTE DE VENTAS", fontTitle);
//        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//
//        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
//        fontParagraph.setSize(12);
//
//        Paragraph paragraph2 = new Paragraph("REPORTE DE VENTA", fontParagraph);
//        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
//
//        document.add(paragraph);
//        document.add(paragraph2);
//        document.close();
//    }

    // Método para generar PDF con filtrado por fechas
    public void generatePDF(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        // Filtrar los datos en función de las fechas
        List<Order> orders = orderRepository.findByOrderDateBetweenAndStatus(startDate, endDate, "PAID");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Título del reporte
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Reporte de Ventas", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Crear tabla para los datos
        PdfPTable table = new PdfPTable(5); // Número de columnas
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Encabezados de la tabla
        Stream.of("Order ID", "Order Date","Amount", "Currency","Status").forEach(header -> {
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.lightGray);
            table.addCell(cell);
        });

        // Poblar datos filtrados
        for (Order order : orders) {

            PdfPCell cell = new PdfPCell(new Phrase(order.getId().toString()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(order.getOrderDate().toString()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(order.getAmount())));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(order.getCurrency())));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(order.getStatus())));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        document.add(table);
        document.close();
    }

}
