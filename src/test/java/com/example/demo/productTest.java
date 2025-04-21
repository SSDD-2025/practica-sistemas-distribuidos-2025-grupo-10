/*
package com.example.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(classes = Demo1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class productTest {
    @LocalServerPort
    int port;
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setupTest() throws InterruptedException {
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://localhost:" + port + "/");
        //  Primera vez, pero que aplica para siempre XD
        driver.findElement(By.id("details-button")).click();
        driver.findElement(By.id("proceed-link")).click();

        //  Iniciar sesion como admin

        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.linkText("Iniciar sesión")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("adminpass");
        driver.findElement(By.cssSelector(".btn:nth-child(4)")).click();
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    //  This test depends on SampleDataService, thus do not touch it
    @Test
    @DisplayName("Save new product without image")
    void testSaveNewProduct() throws InterruptedException {
        driver.get("https://localhost:" + port + "/products/add");
        driver.findElement(By.id("name")).click();
        String aleatorio = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        driver.findElement(By.id("name")).sendKeys(aleatorio);
        driver.findElement(By.id("price")).click();
        driver.findElement(By.id("price")).sendKeys("123456");
        driver.findElement(By.id("category")).click();
        {
            WebElement dropdown = driver.findElement(By.id("category"));
            dropdown.findElement(By.xpath("//option[. = 'Discos']")).click();
        }
        driver.findElement(By.cssSelector(".btn:nth-child(6)")).click();
        driver.findElement(By.linkText("Inicio")).click();
        driver.findElement(By.cssSelector(".fa-bars")).click();
        driver.findElement(By.linkText("Ver productos")).click();

        //  Assert
        String pageSource = driver.getPageSource();
        assertTrue("Texto no encontrado", pageSource.contains(aleatorio));
    }

    @Test
    @DisplayName("Delete an existing product")
    void testDeleteAProduct() throws InterruptedException {
        driver.get("https://localhost:" + port + "/");
        driver.findElement(By.cssSelector(".fa-bars")).click();
        driver.findElement(By.linkText("Añadir productos")).click();
        driver.findElement(By.linkText("Modificar productos")).click();
        driver.findElement(By.cssSelector(".list-group-item:nth-child(1) form > .btn")).click();
        driver.findElement(By.linkText("Ver productos")).click();

        //  Assert
        String pageSource = driver.getPageSource();
        assertFalse("Texto no encontrado", pageSource.contains("Producto Ejemplo 1"));
    }
}
*/