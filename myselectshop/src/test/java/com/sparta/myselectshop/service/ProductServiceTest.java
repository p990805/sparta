package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import com.sparta.myselectshop.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    ProductFolderRepository productFolderRepository;

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void test1() {
        // given
        Long productId = 100L;
        int myprice = ProductService.MIN_MY_PRICE + 3_000_000;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository);

        // when
        ProductResponseDto result = productService.updateProduct(productId, requestMyPriceDto);

        // then
        assertEquals(myprice, result.getMyprice());
    }

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void test2() {
        // given
        Long productId = 200L;
        int myprice = ProductService.MIN_MY_PRICE - 50;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, requestMyPriceDto);
        });

        // then
        assertEquals(
                "유효하지 않은 관심 가격입니다. 최소 " +ProductService.MIN_MY_PRICE + " 원 이상으로 설정해 주세요.",
                exception.getMessage()
        );
    }
}