package exercise.mapper;

// BEGIN
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.model.Product;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductMapper {
    @Mapping(target = "price", source = "cost")
    @Mapping(target = "title", source = "name")
    public abstract Product map(ProductCreateDTO dto);
    @Mapping(target = "price", source = "cost")
    @Mapping(target = "title", source = "name")
    public abstract ProductDTO map(Product model);
    @Mapping(target = "price", source = "cost")
    public abstract void update(ProductUpdateDTO dto, @MappingTarget Product model);
}
// END
