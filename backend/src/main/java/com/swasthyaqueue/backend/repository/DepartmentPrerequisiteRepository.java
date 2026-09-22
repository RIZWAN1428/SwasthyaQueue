package com.swasthyaqueue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.DepartmentPrerequisite;
import java.util.List;

public interface DepartmentPrerequisiteRepository extends JpaRepository<DepartmentPrerequisite, Long>{

    //findByDepartment :- give me all the prerequisites rules for this specific department
    List<DepartmentPrerequisite> findByDepartment(Department department);

    //Check link between departments
    boolean existsByDepartmentAndPrerequisiteDepartment(Department department, Department prerequisiteDepartment);
}