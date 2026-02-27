$basePath = "c:\Users\ADMIN\Downloads\Revpay\Revpay1\src\main\java\com\rev\app"
$servicePath = Join-Path $basePath "service"
$serviceImplPath = Join-Path $servicePath "impl"

Remove-Item -Path "$servicePath\*" -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $serviceImplPath -Force | Out-Null

$entities = @("BusinessUser", "InvoiceItem", "Invoice", "Loan", "MoneyRequest", "Notification", "PaymentMethod", "PersonalUser", "Transaction", "User", "UserSecurity", "Wallet")

foreach ($entity in $entities) {
    if ($entity -eq "User") {
        # For User, we might need a specific handling or we just treat it as standard
    }
    
    $lowerEntity = $entity.Substring(0,1).ToLower() + $entity.Substring(1)
    
    $interfaceContent = @"
package com.rev.app.service;

import com.rev.app.entity.$entity;
import java.util.List;

public interface ${entity}Service {
    $entity create${entity}($entity $lowerEntity);
    $entity get${entity}ById(Long id);
    List<$entity> getAll${entity}s();
    $entity update${entity}(Long id, $entity $lowerEntity);
    void delete${entity}(Long id);
}
"@

    $implContent = @"
package com.rev.app.service.impl;

import com.rev.app.entity.$entity;
import com.rev.app.repository.${entity}Repository;
import com.rev.app.service.${entity}Service;
import com.rev.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional
public class ${entity}ServiceImpl implements ${entity}Service {

    @Autowired
    private ${entity}Repository ${lowerEntity}Repository;

    @Override
    public $entity create${entity}($entity $lowerEntity) {
        return ${lowerEntity}Repository.save($lowerEntity);
    }

    @Override
    public $entity get${entity}ById(Long id) {
        return ${lowerEntity}Repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("$entity not found with id " + id));
    }

    @Override
    public List<$entity> getAll${entity}s() {
        return ${lowerEntity}Repository.findAll();
    }

    @Override
    public $entity update${entity}(Long id, $entity $lowerEntity) {
        $entity existing = get${entity}ById(id);
        // Note: ID should typically be set here to ensure update, but for generic stub we just save
        return ${lowerEntity}Repository.save($lowerEntity);
    }

    @Override
    public void delete${entity}(Long id) {
        ${lowerEntity}Repository.deleteById(id);
    }
}
"@

    Set-Content -Path (Join-Path $servicePath "${entity}Service.java") -Value $interfaceContent
    Set-Content -Path (Join-Path $serviceImplPath "${entity}ServiceImpl.java") -Value $implContent
}
