"use strict";

// Class definition
var KTAppEcommerceProducts = function () {
    // Shared variables
    var table;
    var datatable;

    // Private functions
    var initDatatable = function () {
        // Init datatable --- more info on datatables: https://datatables.net/manual/
        datatable = $(table).DataTable({
            "info": false,
            'order': [],
            'pageLength': 10,
            'columnDefs': [
                {render: DataTable.render.number(',', '.', 2), targets: 4},
                {orderable: false, targets: 0}, // Disable ordering on column 0 (checkbox)
                {orderable: false, targets: 7}, // Disable ordering on column 7 (actions)
            ]
        });

        // Re-init functions on datatable re-draws
        datatable.on('draw', function () {
            handleDeleteRows();
        });
    }

    // Search Datatable --- official docs reference: https://datatables.net/reference/api/search()
    var handleSearchDatatable = () => {
        const filterSearch = document.querySelector('[data-kt-ecommerce-product-filter="search"]');
        filterSearch.addEventListener('keyup', function (e) {
            datatable.search(e.target.value).draw();
        });
    }

    // Handle status filter dropdown
    var handleStatusFilter = () => {
        const filterStatus = document.querySelector('[data-kt-ecommerce-product-filter="status"]');
        $(filterStatus).on('change', e => {
            let value = e.target.value;
            if (value === 'all') {
                value = '';
            }
            datatable.column(6).search(value).draw();
        });
    }

    // Delete cateogry
    var handleDeleteRows = () => {
        // Select all delete buttons
        const deleteButtons = table.querySelectorAll('[data-kt-ecommerce-product-filter="delete_row"]');

        deleteButtons.forEach(d => {
            // Delete button on click
            d.addEventListener('click', function (e) {
                e.preventDefault();

                // Select parent row
                const parent = e.target.closest('tr');

                // Get category name
                const productName = parent.querySelector('[data-kt-ecommerce-product-filter="product_name"]').innerText;

                // SweetAlert2 pop up --- official docs reference: https://sweetalert2.github.io/
                Swal.fire({
                    text: "Are you sure you want to delete " + productName + "?",
                    icon: "warning",
                    showCancelButton: true,
                    buttonsStyling: false,
                    confirmButtonText: "Yes, delete!",
                    cancelButtonText: "No, cancel",
                    customClass: {
                        confirmButton: "btn fw-bold btn-danger",
                        cancelButton: "btn fw-bold btn-active-light-primary"
                    }
                }).then(function (result) {
                    if (result.value) {
                        Swal.fire({
                            text: "You have deleted " + productName + "!.",
                            icon: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Ok, got it!",
                            customClass: {
                                confirmButton: "btn fw-bold btn-primary",
                            }
                        }).then(function () {
                            // Remove current row
                            datatable.row($(parent)).remove().draw();
                        });
                    } else if (result.dismiss === 'cancel') {
                        Swal.fire({
                            text: productName + " was not deleted.",
                            icon: "error",
                            buttonsStyling: false,
                            confirmButtonText: "Ok, got it!",
                            customClass: {
                                confirmButton: "btn fw-bold btn-primary",
                            }
                        });
                    }
                });
            })
        });
    }


    // Public methods
    return {
        init: function () {
            table = document.querySelector('#kt_ecommerce_products_table');

            if (!table) {
                return;
            }

            initDatatable();
            handleSearchDatatable();
            handleStatusFilter();
            handleDeleteRows();
        }
    };
}();

// On document ready
KTUtil.onDOMContentLoaded(function () {
    KTAppEcommerceProducts.init();
});


    <!-- Begin Check Box Selection -->
    document.addEventListener("DOMContentLoaded", function () {
    // Get the header checkbox and all product checkboxes
    const selectAllCheckbox = document.querySelector('thead .form-check-input[type="checkbox"]');
    const productCheckboxes = document.querySelectorAll('tbody .form-check-input[type="checkbox"]');

    // Add event listener for the select-all checkbox
    selectAllCheckbox.addEventListener('change', function () {
    const isChecked = selectAllCheckbox.checked;
    // Check/uncheck all product checkboxes based on the header checkbox status
    productCheckboxes.forEach(checkbox => {
    checkbox.checked = isChecked;
    });
    });

    // Add event listeners to individual product checkboxes to update the header checkbox state
    productCheckboxes.forEach(checkbox => {
    checkbox.addEventListener('change', function () {
    // If any product checkbox is unchecked, uncheck the header checkbox
    if (!checkbox.checked) {
    selectAllCheckbox.checked = false;
    } else {
        // If all product checkboxes are checked, check the header checkbox
        const allChecked = Array.from(productCheckboxes).every(checkbox => checkbox.checked);
        selectAllCheckbox.checked = allChecked;
    }
    });
    });
    });
    <!-- End Check Box Selection  -->

    <!-- Begin Delete Many Products  -->
    document.addEventListener("DOMContentLoaded", function () {
    const bulkDeleteButton = document.getElementById("bulkDeleteButton");
    const productCheckboxes = document.querySelectorAll('tbody .form-check-input[type="checkbox"]');
    const deleteProductIdsInput = document.getElementById("deleteProductIds");
    const deleteConfirmationText = document.getElementById("deleteConfirmationText");

    bulkDeleteButton.addEventListener("click", function () {
    // Get all selected product IDs
    const selectedIds = Array.from(productCheckboxes)
    .filter(checkbox => checkbox.checked)
    .map(checkbox => checkbox.closest('tr').querySelector('td:nth-child(3)').innerText.trim());

    // Update hidden input with selected IDs
    deleteProductIdsInput.value = selectedIds.join(",");

    // Update modal text based on the number of selected products
    if (selectedIds.length === 1) {
    deleteConfirmationText.textContent = "Êtes-vous sûr de vouloir supprimer ce produit ?";
    } else if (selectedIds.length > 1) {
        deleteConfirmationText.textContent = `Êtes-vous sûr de vouloir supprimer les ${selectedIds.length} produits ?`;
    } else {
        deleteConfirmationText.textContent = "Veuillez sélectionner au moins un produit à supprimer.";
    }

    // Disable the delete button if no product is selected
    bulkDeleteButton.disabled = selectedIds.length === 0;
    });
    });
    <!-- End Delete Many Products  -->
    $('#deleteProductModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const productId = button.data('product-id');
        const productName = button.data('product-name');
        const modal = $(this);
        modal.find('#deleteProductId').val(productId);
        modal.find('#deleteProductName').text(productName);
    });

    <!-- Begin Add Product Modal  -->
    $(document).ready(function () {
        // Handle "Add Product" button click
        $('#addProductButton').on('click', function () {
            $('#productForm')[0].reset();
            $('#productIdInput').val('');
            $('#productModalTitle').text('Créer un produit');
            $('#productSaveButton').text('Ajouter');
            $('#productForm').attr('action', '/produits'); // Set form action to add product
            $('#kt_modal_product').modal('show');
        });
    });
    <!-- End Add Product Modal  -->

    // search product js
$(document).ready(function() {
    $("#searchInput").on("input", function() {
        var searchTerm = $(this).val().toLowerCase();
        $("tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().includes(searchTerm));
        });
    });

    // Fonction pour rechercher avec l'URL
    function searchWithUrl() {
        var searchTerm = $("#searchInput").val();
        var url = '/produits';
        if (searchTerm) {
            url += '?search=' + encodeURIComponent(searchTerm);
        }
        window.location.href = url;
    }

    // Appellez cette fonction lors du clic sur le bouton de recherche
    $('#searchButton').click(function() { searchWithUrl(); });
});

    // update product
$(document).on('click', 'a[data-bs-target="#updateProductModal"]', function () {
    const productId = $(this).data('id');
    const productName = $(this).data('name');
    const productDescription = $(this).data('description');
    const productQuantity = $(this).data('quantite');
    const productPrice = $(this).data('prix');
    const fournisseurId = $(this).data('fournisseur-id');

    // Pré-remplir le formulaire avec les données du produit
    $('#productIdToUpdate').val(productId);
    $('#produitNameInput').val(productName);
    $('#produitDescriptionInput').val(productDescription);
    $('#produitQuantityInput').val(productQuantity);
    $('#produitPriceInput').val(productPrice);

    // Récupérer le nom du fournisseur du produit à modifier
    const defaultFournisseur = $(this).find('span[data-bs-toggle="tooltip"]').attr('title');
    $('#fournisseurSelection').val(fournisseurId).trigger('change');

    // Récupérer tous les fournisseurs
    $.ajax({
        url: '/fournisseurs',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            const fournisseurSelect = $('#fournisseurSelection');
            fournisseurSelect.empty();
            fournisseurSelect.append($('<option>', { value: '', text: 'Sélectionnez un fournisseur' }));
            data.forEach(fournisseur => {
                fournisseurSelect.append($('<option>', {
                    value: fournisseur.id,
                    text: fournisseur.nom
                }));
            });
            fournisseurSelect.val(fournisseurId).trigger('change');
        },
        error: function(xhr, status, error) {
            console.error("Erreur lors de la récupération des fournisseurs:", error);
        }
    });

    // Mettre à jour le titre du modal
    $('#updateProductModal .modal-title').text('Modifier le produit');
});
