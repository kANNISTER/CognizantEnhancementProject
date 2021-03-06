package com.cts.mfpe.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.mfpe.exception.AuthorizationException;
import com.cts.mfpe.exception.IPTreatmentPackageNotFoundException;
import com.cts.mfpe.feign.AuthorisingClient;
import com.cts.mfpe.model.AilmentCategory;
import com.cts.mfpe.model.IPTreatmentPackage;
import com.cts.mfpe.model.PackageDetail;
import com.cts.mfpe.model.SpecialistDetail;
import com.cts.mfpe.service.IPTreatmentOfferingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class IPTreatmentOfferingController {

	@Autowired
	private IPTreatmentOfferingService ipOfferingService;

	@Autowired
	private AuthorisingClient authorisingClient;

	/**
	 * @param requestTokenHeader
	 * @return
	 * @throws AuthorizationException
	 * @throws Exception
	 */
	@GetMapping("/ipTreatmentPackages")
	@ApiOperation(notes = "Returns the list of IP Treatment Packages", value = "Find IP Treatment Package")
	public List<IPTreatmentPackage> getAllIPTreatmentPackage(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader)
			throws AuthorizationException {
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			return ipOfferingService.findAllIPTreatmentPackages();
		} else {
			throw new AuthorizationException("Not allowed");
		}

	}

	/**
	 * @param ailment
	 * @param packageName
	 * @param requestTokenHeader
	 * @return
	 * @throws AuthorizationException
	 * @throws IPTreatmentPackageNotFoundException
	 * @throws Exception
	 */
	@GetMapping("/ipTreatmentPackageByName/{ailment}/{packageName}")
	@ApiOperation(notes = "Returns the IP Treatment Package based on package name", value = "Find IP Treatment Package by name")
	public IPTreatmentPackage getIPTreatmentPackageByName(
			@ApiParam(name = "ailment", value = "ailment of the package") @PathVariable AilmentCategory ailment,
			@ApiParam(name = "packageName", value = "name of the package") @PathVariable String packageName,
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader)
			throws AuthorizationException, IPTreatmentPackageNotFoundException {

		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			return ipOfferingService.findIPTreatmentPackageByName(ailment, packageName);
		} else {
			throw new AuthorizationException("Not allowed");
		}
	}

	/**
	 * @param requestTokenHeader
	 * @return
	 * @throws AuthorizationException
	 * @throws Exception
	 */
	@GetMapping("/specialists")
	@ApiOperation(notes = "Returns the list of specialists along with their experience and contact details", value = "Find specialists")
	public List<SpecialistDetail> getAllSpecialist(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader)
			throws AuthorizationException {
		System.out.println("Inside ================"+requestTokenHeader);
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			return ipOfferingService.findAllSpecialists();
		} else {
			throw new AuthorizationException("Not allowed");
		}
	}

	@GetMapping("/health-check")
	public ResponseEntity<String> healthCheck() {
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}
	
	@GetMapping("/specialistsByExpertsise/{areaOfExpertise}")
	@ApiOperation(notes = "Returns the list of specialists by expertsise", value = "Specialists By Expertsise")
	public List<SpecialistDetail> specialistsByExpertsise(
			@ApiParam(name = "areaOfExpertise", value = "ailment of the package") @PathVariable AilmentCategory areaOfExpertise,
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader)
			throws AuthorizationException{
		
		System.out.println("Inside ================"+requestTokenHeader);
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			return ipOfferingService.findSpecialistsByExpertise(areaOfExpertise);
		} else {
			throw new AuthorizationException("Not allowed");
		}
		
	}
	
	@PostMapping("/addspecialist")
	@ApiOperation(notes = "Adds a new specialist", value = "Add Specialist")
	public ResponseEntity<String> addSpecialist(@ApiParam(name = "specialistDetail", value = "Specialist Detail") 
				@Valid @RequestBody SpecialistDetail specialistDetail,
				@RequestHeader(value = "Authorization", required = true) String requestTokenHeader)
				throws AuthorizationException{
		System.out.println("Inside ================"+requestTokenHeader);
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			if(ipOfferingService.addSpecialist(specialistDetail)) {
				return new ResponseEntity<>("Specialist added successfully!!", HttpStatus.OK);
			}else {
				return new ResponseEntity<>("Specialist cannot be added", HttpStatus.FORBIDDEN);
			}
		} else {
			throw new AuthorizationException("Not allowed");
		}
	}
	
	@DeleteMapping("/deleteSpecialist/{id}")
	@ApiOperation(notes = "Delete existing Specialist", value = "Delete Specialist")
	public ResponseEntity<String> deleteSpecialistById(@ApiParam(name = "id", value = "Specialist id") 
					@PathVariable int id,
					@RequestHeader(value = "Authorization", required = true) String requestTokenHeader)
							throws AuthorizationException{
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			if(ipOfferingService.deleteSpecialistById(id)) {
				return new ResponseEntity<>("Specialist deleted successfully!!", HttpStatus.OK);
			}else {
				return new ResponseEntity<>("Specialist cannot be deleted or not present in database", HttpStatus.FORBIDDEN);
			}
		} else {
			throw new AuthorizationException("Not allowed");
		}
	}
	
	
	@GetMapping("/getallpackages")
	@ApiOperation(notes = "Get All Packages Available", value = "getallpackages")
	public List<PackageDetail> getAllPackages
			(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) 
			throws AuthorizationException{
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			return ipOfferingService.getAllPackages();
		} else {
			throw new AuthorizationException("Not allowed");
		}
	}
  
	@PutMapping("/updateIpTreatmentPackage/{id}")
	@ApiOperation(notes = "Update Package", value = "update package")
	public IPTreatmentPackage updatePackage(
			@ApiParam(name = "id", value = "Specialist id") @PathVariable int id,
			@ApiParam(name = "packageDetail", value = "Package object") @Valid @RequestBody IPTreatmentPackage ipTreatmentPackage,
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) throws AuthorizationException, IPTreatmentPackageNotFoundException {
		
		if (authorisingClient.authorizeTheRequest(requestTokenHeader)) {
			return ipOfferingService.updateIPTreatmentPackage(id, ipTreatmentPackage);
		} else {
			throw new AuthorizationException("Not allowed");
		}
		
	}
	

	
}
