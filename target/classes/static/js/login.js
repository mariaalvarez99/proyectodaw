function mostrarFormulario(formulario) {

    document.getElementById('btnacceso').classList.remove('active');
    document.getElementById('btnnuevo').classList.remove('active');

    // Oculta todos los formularios
    document.getElementById('formacceso').style.display = 'none';
    document.getElementById('formnuevo').style.display = 'none';

    // Muestra el formulario correspondiente al botón pulsado
    document.getElementById('form' + formulario).style.display = 'block';

    //add clase active
    document.getElementById('btn' + formulario).className += " active";

}